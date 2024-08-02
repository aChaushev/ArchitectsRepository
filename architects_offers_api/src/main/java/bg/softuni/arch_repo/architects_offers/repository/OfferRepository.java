package bg.softuni.arch_repo.architects_offers.repository;

import bg.softuni.arch_repo.architects_offers.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

}
