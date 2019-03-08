package nl.appsource.awesome.bootdocker.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import nl.appsource.awesome.bootdocker.model.Citaat;

@Repository
public interface CitaatRepository extends CrudRepository<Citaat, Long>, QuerydslPredicateExecutor<Citaat>  {
    
    @Query("SELECT id FROM Citaat")
    List<Long> findAllId(final Pageable pageRequest);
    
}
