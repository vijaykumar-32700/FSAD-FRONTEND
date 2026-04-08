package com.peer.repository;

import com.peer.entity.PeerReview;
import com.peer.entity.Submission;
import com.peer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeerReviewRepository extends JpaRepository<PeerReview, Long> {
    List<PeerReview> findByReviewerOrderByCreatedAtDesc(User reviewer);
    List<PeerReview> findBySubmission(Submission submission);

    boolean existsBySubmissionAndReviewer(Submission submission, User reviewer);

    @Query("SELECT pr FROM PeerReview pr WHERE pr.reviewer = :reviewer AND pr.status = 'PENDING'")
    List<PeerReview> findPendingByReviewer(User reviewer);

    @Query("SELECT pr FROM PeerReview pr WHERE pr.reviewer = :reviewer AND pr.status = 'COMPLETED'")
    List<PeerReview> findCompletedByReviewer(User reviewer);

    @Query("SELECT AVG(pr.score) FROM PeerReview pr WHERE pr.submission.student = :student AND pr.status = 'COMPLETED'")
    Double findAveragePeerScoreByStudent(User student);
}