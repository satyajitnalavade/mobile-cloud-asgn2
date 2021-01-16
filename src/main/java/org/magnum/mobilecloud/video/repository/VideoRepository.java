package org.magnum.mobilecloud.video.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author satya
 */
@Repository
public interface VideoRepository extends CrudRepository<Video, Long> {

    List<Video> findByName(String name);

    List<Video> findByDurationLessThan(long duration);
}
