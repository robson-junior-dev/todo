package br.dev.robsonjunior.todo.service;

import br.dev.robsonjunior.todo.domain.Task;
import br.dev.robsonjunior.todo.repository.TaskRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Task}.
 */
@Service
@Transactional
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Save a task.
     *
     * @param task the entity to save.
     * @return the persisted entity.
     */
    public Task save(Task task) {
        log.debug("Request to save Task : {}", task);
        return taskRepository.save(task);
    }

    /**
     * Update a task.
     *
     * @param task the entity to save.
     * @return the persisted entity.
     */
    public Task update(Task task) {
        log.debug("Request to update Task : {}", task);
        return taskRepository.save(task);
    }

    /**
     * Partially update a task.
     *
     * @param task the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Task> partialUpdate(Task task) {
        log.debug("Request to partially update Task : {}", task);

        return taskRepository
            .findById(task.getId())
            .map(existingTask -> {
                if (task.getName() != null) {
                    existingTask.setName(task.getName());
                }
                if (task.getDescription() != null) {
                    existingTask.setDescription(task.getDescription());
                }
                if (task.getStatus() != null) {
                    existingTask.setStatus(task.getStatus());
                }
                if (task.getHoursToComplete() != null) {
                    existingTask.setHoursToComplete(task.getHoursToComplete());
                }
                if (task.getAttachment() != null) {
                    existingTask.setAttachment(task.getAttachment());
                }
                if (task.getAttachmentContentType() != null) {
                    existingTask.setAttachmentContentType(task.getAttachmentContentType());
                }
                if (task.getImage() != null) {
                    existingTask.setImage(task.getImage());
                }
                if (task.getImageContentType() != null) {
                    existingTask.setImageContentType(task.getImageContentType());
                }

                return existingTask;
            })
            .map(taskRepository::save);
    }

    /**
     * Get all the tasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Task> findAll(Pageable pageable) {
        log.debug("Request to get all Tasks");
        return taskRepository.findAll(pageable);
    }

    /**
     * Get all the tasks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Task> findAllWithEagerRelationships(Pageable pageable) {
        return taskRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one task by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Task> findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        return taskRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the task by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }
}
