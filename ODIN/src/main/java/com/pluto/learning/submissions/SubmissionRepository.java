package com.pluto.learning.submissions;

import com.pluto.learning.assessments.Lab;
import com.pluto.learning.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<LabSubmission, Long> {
    
    /**
     * Contar envíos por laboratorio y usuario
     */
    int countByLabAndUser(Lab lab, User user);
    
    /**
     * Obtener envíos por laboratorio y usuario ordenados por número de intento descendente
     */
    List<LabSubmission> findByLabAndUserOrderByAttemptNumberDesc(Lab lab, User user);
    
    /**
     * Obtener envíos por estado ordenados por fecha de envío
     */
    List<LabSubmission> findByStatusOrderBySubmittedAtAsc(LabSubmission.SubmissionStatus status);
    
    /**
     * Contar envíos por laboratorio
     */
    int countByLab(Lab lab);
    
    /**
     * Contar envíos por laboratorio y estado
     */
    int countByLabAndStatus(Lab lab, LabSubmission.SubmissionStatus status);
    
    /**
     * Obtener promedio de puntuación final por laboratorio
     */
    @Query("SELECT AVG(s.finalScore) FROM LabSubmission s WHERE s.lab = :lab AND s.finalScore IS NOT NULL")
    Double findAverageScoreByLab(@Param("lab") Lab lab);
    
    /**
     * Obtener envíos por usuario ordenados por fecha de envío descendente
     */
    List<LabSubmission> findByUserOrderBySubmittedAtDesc(User user);
    
    /**
     * Obtener último envío por laboratorio y usuario
     */
    @Query("SELECT s FROM LabSubmission s WHERE s.lab = :lab AND s.user = :user ORDER BY s.attemptNumber DESC LIMIT 1")
    LabSubmission findLatestSubmissionByLabAndUser(@Param("lab") Lab lab, @Param("user") User user);
    
    /**
     * Obtener envíos aprobados por usuario
     */
    List<LabSubmission> findByUserAndStatusOrderBySubmittedAtDesc(User user, LabSubmission.SubmissionStatus status);
    
    /**
     * Verificar si usuario ha aprobado el laboratorio
     */
    @Query("SELECT COUNT(s) > 0 FROM LabSubmission s WHERE s.lab = :lab AND s.user = :user AND s.status = 'APPROVED'")
    boolean hasUserPassedLab(@Param("lab") Lab lab, @Param("user") User user);
    
    /**
     * Obtener envíos que necesitan revisión manual
     */
    @Query("SELECT s FROM LabSubmission s WHERE s.status IN ('REVIEW_PENDING', 'EVALUATED') AND s.lab.evaluationType IN ('MANUAL', 'HYBRID') ORDER BY s.submittedAt ASC")
    List<LabSubmission> findSubmissionsNeedingManualReview();
}
