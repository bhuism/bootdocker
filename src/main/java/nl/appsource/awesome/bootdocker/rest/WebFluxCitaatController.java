package nl.appsource.awesome.bootdocker.rest;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static reactor.function.TupleUtils.function;

import java.net.URI;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import nl.appsource.awesome.bootdocker.model.Citaat;
import nl.appsource.awesome.bootdocker.repository.CitaatRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest")
public class WebFluxCitaatController {

    @Inject
    private CitaatRepository citaatRepository;

    private static final int DEFAULT_LIMIT = 5;

    private static final String ROOT = "citaten";
    
    private static final Pageable DEFAULT_PAGEREQUEST = PageRequest.of(0, DEFAULT_LIMIT);

    @GetMapping("/" + ROOT)
    public Mono<CollectionModel<EntityModel<Citaat>>> all(@PageableDefault(size = DEFAULT_LIMIT) final Pageable pageable) {

        WebFluxCitaatController controller = methodOn(WebFluxCitaatController.class);

        return Flux.fromIterable(citaatRepository.findAllId(pageable)) //
                .flatMap(id -> findOne(id)) //
                .collectList() //
                .flatMap(resources -> linkTo(controller.all(DEFAULT_PAGEREQUEST)).withSelfRel() //
                        .andAffordance(controller.newCitaat(null)) //
                        .andAffordance(controller.search(null, null)) //
                        .toMono() //
                        .map(selfLink -> new CollectionModel<>(resources, selfLink)));
    }

    @GetMapping("/" + ROOT + "/search")
    public Mono<CollectionModel<EntityModel<Citaat>>> search( //
            @RequestParam Optional<String> name, //
            @PageableDefault(size = DEFAULT_LIMIT) final Pageable pageable) {

        WebFluxCitaatController controller = methodOn(WebFluxCitaatController.class);

        return Flux.fromIterable(citaatRepository.findAllId(pageable)) //
                .flatMap(id -> findOne(id)) //
                .filter(resource -> {

                    boolean nameMatches = name //
                            .map(s -> resource.getContent().getName().contains(s)) //
                            .orElse(true);

                    return nameMatches;
                }).collectList().flatMap(resources -> {
                    return linkTo(controller.all(DEFAULT_PAGEREQUEST)).withSelfRel() //
                            .andAffordance(controller.newCitaat(null)) //
                            .andAffordance(controller.search(null, null)) //
                            .toMono() //
                            .map(selfLink -> new CollectionModel<>(resources, selfLink));
                });
    }

    @GetMapping("/" + ROOT + "/{id}")
    public Mono<EntityModel<Citaat>> findOne(@PathVariable Long id) {

        WebFluxCitaatController controller = methodOn(WebFluxCitaatController.class);

        Mono<Link> selfLink = linkTo(controller.findOne(id)).withSelfRel() //
                .andAffordance(controller.updateCitaat(null, id)) //
                .andAffordance(controller.partiallyUpdateCitaat(null, id)) //
                .toMono();

        Mono<Link> citatenLink = linkTo(controller.all(DEFAULT_PAGEREQUEST)).withRel(ROOT) //
                .toMono();

        return selfLink.zipWith(citatenLink) //
                .map(function((left, right) -> Links.of(left, right))) //
                .map(links -> {
                    return new EntityModel<>(citaatRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Citaat Not Found")), links);
                });
    }

    @PostMapping("/" + ROOT)
    public Mono<ResponseEntity<?>> newCitaat(@RequestBody Mono<EntityModel<Citaat>> citaat) {

        return citaat.flatMap(resource -> {
            final Long id = citaatRepository.save(resource.getContent()).getId();
            return findOne(id);

        }).map(findOne -> {

            return ResponseEntity.created(URI.create(findOne //
                    .getLink(IanaLinkRelations.SELF) //
                    .map(link -> link.expand().getHref()) //
                    .orElse(""))) //
                    .build();
        });
    }

    @PutMapping("/" + ROOT + "/{id}")
    public Mono<ResponseEntity<?>> updateCitaat(@RequestBody Mono<EntityModel<Citaat>> citaat, @PathVariable Long id) {

        return citaat.flatMap(resource -> {
            citaatRepository.save(resource.getContent());
            return findOne(id);
        }).map(findOne -> {

            return ResponseEntity.noContent() //
                    .location(URI.create(findOne //
                            .getLink(IanaLinkRelations.SELF) //
                            .map(link -> link.expand().getHref()) //
                            .orElse(""))) //
                    .build();
        });
    }

    @PatchMapping("/" + ROOT + "/{id}")
    public Mono<ResponseEntity<?>> partiallyUpdateCitaat( //
            @RequestBody Mono<EntityModel<Citaat>> citaat, @PathVariable Long id) {

        return citaat.flatMap(resource -> {

            Citaat oldCitaat = citaatRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Citaat Not Found"));
            Citaat newCitaat = oldCitaat;

            newCitaat.setId(id);

            if (resource.getContent().getName() != null) {
                newCitaat = newCitaat.withName(resource.getContent().getName());
            }

            citaatRepository.save(newCitaat);

            return findOne(id);

        }).map(findOne -> {

            return ResponseEntity.noContent() //
                    .location(URI.create(findOne //
                            .getLink(IanaLinkRelations.SELF) //
                            .map(link -> link.expand().getHref()) //
                            .orElse(""))) //
                    .build();
        });
    }
}