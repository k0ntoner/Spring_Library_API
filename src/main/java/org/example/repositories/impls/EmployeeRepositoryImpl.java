package org.example.repositories.impls;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Employee;
import org.example.exceptions.RepositoryException;
import org.example.repositories.EmployeeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public EmployeeRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public Employee save(Employee entity){
        log.debug("Saving Employee {}", entity);
        try{
            sessionFactory.getCurrentSession().persist(entity);
            return entity;
        }
        catch (Exception e){
            log.error("Error while trying to save Employee {}", entity, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Employee> findById(String id){
        log.debug("Fetching Employee by id {}", id);
        try(Session session = sessionFactory.openSession()){
            Employee employee = session.get(Employee.class, id);
            return Optional.ofNullable(employee);
        }
        catch (Exception e){
            log.error("Error while trying to fetch Employee by id {}", id, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Employee update(Employee entity){
        log.debug("Updating Employee {}", entity);
        try{
            if(entity.getId()==null){
                throw new IllegalArgumentException("Employee must have an id");
            }
            entity = sessionFactory.getCurrentSession().merge(entity);
            return entity;
        }
        catch (Exception e){
            log.error("Error while trying to update Employee {}", entity, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void delete(Employee entity){
        log.debug("Deleting Employee {}", entity);
        try{
            Session session = sessionFactory.getCurrentSession();
            if(!session.contains(entity)){
                entity=session.merge(entity);
            }
            session.remove(entity);
        }
        catch (Exception e){
            log.error("Error while trying to delete Employee {}", entity, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public Collection<Employee> findAll(){
        log.debug("Fetching all Employees");
        try(Session session = sessionFactory.openSession()){
            Collection<Employee> employees = session.createQuery("from Employee ").getResultList();
            return employees;
        }
        catch (Exception e){
            log.error("Error while trying to fetch all Employees", e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }
}
