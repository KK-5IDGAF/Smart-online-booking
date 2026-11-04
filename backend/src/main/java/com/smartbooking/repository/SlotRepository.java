package com.smartbooking.repository;

import com.smartbooking.entity.Slot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Query("SELECT s FROM Slot s JOIN FETCH s.resource")
    List<Slot> findAllWithResource();

    @Query("SELECT s FROM Slot s JOIN FETCH s.resource WHERE s.available = true")
    List<Slot> findAvailableWithResource();

    @Query("SELECT s FROM Slot s JOIN FETCH s.resource WHERE s.resource.id = :resourceId")
    List<Slot> findByResourceId(@Param("resourceId") Long resourceId);

    @Query("SELECT s FROM Slot s JOIN FETCH s.resource WHERE s.resource.id = :resourceId AND s.slotDate = :slotDate")
    List<Slot> findByResourceIdAndSlotDate(@Param("resourceId") Long resourceId, @Param("slotDate") LocalDate slotDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Slot s JOIN FETCH s.resource WHERE s.id = :id")
    Optional<Slot> findByIdForUpdate(@Param("id") Long id);
}
