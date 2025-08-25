package com.pluto.learning.submissions;

/**
 * Estados posibles de una submission
 */
public enum SubmissionStatus {
    PENDING,        // Enviada, pendiente de evaluación
    GRADING,        // En proceso de evaluación automática
    GRADED,         // Evaluada automáticamente
    MANUAL_REVIEW,  // Requiere revisión manual
    APPROVED,       // Aprobada
    REJECTED,       // Rechazada
    RESUBMIT        // Requiere reenvío
}
