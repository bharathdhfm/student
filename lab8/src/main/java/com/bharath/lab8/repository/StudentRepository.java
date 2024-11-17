package com.bharath.lab8.repository;

import com.bharath.lab8.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
	 