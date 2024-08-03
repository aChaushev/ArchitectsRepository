package bg.softuni.arch_repo.architects_offers.repository;

import bg.softuni.arch_repo.architects_offers.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Offer o WHERE o.created < :olderThan")
    void deleteOldOffers(Instant olderThan);
}
