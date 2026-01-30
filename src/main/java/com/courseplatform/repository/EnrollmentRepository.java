package com.courseplatform.repository;

import com.courseplatform.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.course.id = :courseId")
    Optional<Enrollment> findByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") String courseId);

    boolean existsByUserIdAndCourseId(Long userId, String courseId);

    List<Enrollment> findByUserId(Long userId);
}
