package io.ppmtool.code.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.ppmtool.code.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project,Long> {

	 Iterable<Project> findAll();
	 
	 Iterable<Project> findByProjectLeader(String username);
	 
	 Project findByProjectIdentifier(String projectId);

}
