package com.fcgl.madrid.points.service;

import com.fcgl.madrid.points.model.UserPoint;
import com.fcgl.madrid.points.repository.UserPointsRepository;
import com.fcgl.madrid.points.model.InternalStatus;
import com.fcgl.madrid.points.model.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.lang.Exception;
import java.lang.Throwable;
import java.lang.StringBuilder;

@Service
public class UserPointsService {
    private UserPointsRepository userPointsRepository;

    @Autowired
    public UserPointsService(UserPointsRepository userPointsRepository) {
        this.userPointsRepository = userPointsRepository;
    }

    public List<UserPoint> findAll() {
        return userPointsRepository.findAll();
    }

    // @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    // public ResponseEntity<InternalStatus>

    /**
    *@param
    *@return
    */
    private ResponseEntity<InternalStatus> handleParamException(TransactionSystemException e) {
      return getInternalStatusResponseEntity(e);
    }
    private ResponseEntity<InternalStatus> getInternalStatusResponseEntity(TransactionSystemException e) {
      Throwable cause = e.getRootCause();
      if (cause instanceof ConstraintViolationException) {
        Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
        List<String> messages = new ArrayList<String>();
        for (ConstraintViolation v : constraintViolations) {
          StringBuilder builder = new StringBuilder("");
          builder.append(v.getPropertyPath().toString());
          builder.append(" ");
          builder.append(v.getMessage());
          messages.add(builder.toString());
        }
            // do something here
            InternalStatus internalStatus = new InternalStatus(StatusCode.PARAM, 400, messages);
            return new ResponseEntity<InternalStatus>(internalStatus, HttpStatus.BAD_REQUEST);
        }
        InternalStatus internalStatus = new InternalStatus(StatusCode.UNKNOWN, 500, e.getMessage());
        return new ResponseEntity<InternalStatus>(internalStatus, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
