package springboot.thymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.thymeleaf.entity.AuthoritiesEntity;

@Repository
public interface UserAuthoritiesRepository extends JpaRepository<AuthoritiesEntity, Long> {
}
