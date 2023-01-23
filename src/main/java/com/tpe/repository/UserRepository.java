package com.tpe.repository;

import com.tpe.domain.*;
import com.tpe.exception.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUserName(String userName) throws ResourceNotFoundException;

    Boolean existsByUserName(String userName);
}
