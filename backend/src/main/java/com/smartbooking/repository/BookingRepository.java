package com.smartbooking.repository;

import com.smartbooking.entity.Booking;
import com.smartbooking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Booking> findAllByOrderByCreatedAtDesc();

    boolean existsBySlotIdAndStatusIn(Long slotId, List<BookingStatus> statuses);

    long countByStatus(BookingStatus status);

    long countByStatusIn(List<BookingStatus> statuses);

    @Query("SELECT b FROM Booking b JOIN FETCH b.user JOIN FETCH b.slot s JOIN FETCH s.resource ORDER BY b.createdAt DESC")
    List<Booking> findAllWithDetails();

    @Query("SELECT b FROM Booking b JOIN FETCH b.user JOIN FETCH b.slot s JOIN FETCH s.resource WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
    List<Booking> findByUserIdWithDetails(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.user JOIN FETCH b.slot s JOIN FETCH s.resource WHERE b.id = :id")
    Optional<Booking> findByIdWithDetails(@Param("id") Long id);
}
