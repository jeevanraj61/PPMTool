package io.ppmtool.code.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.ppmtool.code.domain.Backlog;
import io.ppmtool.code.domain.Project;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog,Long>{

	Backlog findByProjectIdentifier(String identifier);
}
