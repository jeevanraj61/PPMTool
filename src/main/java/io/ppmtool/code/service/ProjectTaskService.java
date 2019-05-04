package io.ppmtool.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.ppmtool.code.domain.Backlog;
import io.ppmtool.code.domain.Project;
import io.ppmtool.code.domain.ProjectTask;
import io.ppmtool.code.exceptions.ProjectIdException;
import io.ppmtool.code.exceptions.ProjectNotFoundException;
import io.ppmtool.code.repositories.BacklogRepository;
import io.ppmtool.code.repositories.ProjectRepository;
import io.ppmtool.code.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ProjectService projectService;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask,String username) {

	
			// Exceptions: Project not found

			// PTs to be added to specific Project, project!=null, BL exists
			
			Backlog backlog = projectService.findByProjectIdentifier(projectIdentifier, username).getBacklog();

			// set BL to pt
			projectTask.setBacklog(backlog);

			// we want our project sequence to be like this IDPRO-1 IDPRO-2 ... 100 101
			Integer backlogSequence = backlog.getPTSequence();

			// Update the BL sequence
			backlogSequence++;
			backlog.setPTSequence(backlogSequence);

			// Add sequence to project task
			projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);

			// Initial priority when priority is null
			if ( projectTask.getPriority() == null || projectTask.getPriority()==0 ) {
				projectTask.setPriority(3);
			}
			// Initial Status when Status is null
			if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}

			return projectTaskRepository.save(projectTask);

		
	}


	public Iterable<ProjectTask> findBacklogById(String backlog_id,String username) {
		
		projectService.findByProjectIdentifier(backlog_id, username);

		return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id,String pt_id,String username) {
		
		projectService.findByProjectIdentifier(backlog_id, username);
		
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task with Id:"+pt_id+" does not exist");			
		}
		
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task "+pt_id+" does not exist in project:'"+backlog_id);
		}
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id,String username) {
		ProjectTask projectTask = this.findPTByProjectSequence(backlog_id, pt_id, username);
		
		projectTask = updatedTask;
		
		return projectTaskRepository.save(projectTask);
	}

	public void deletePTByProjectSequence(String backlog_id, String pt_id,String username) {
		ProjectTask projectTask = this.findPTByProjectSequence(backlog_id, pt_id, username);
		
		projectTaskRepository.delete(projectTask);
	}
}
