package com.ims.servicesupport.repo;
import com.ims.servicesupport.repo.model.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SupportRepo extends JpaRepository<Support, Long> {
}
