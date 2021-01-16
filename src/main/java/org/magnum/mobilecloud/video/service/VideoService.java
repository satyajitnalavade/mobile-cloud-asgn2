package org.magnum.mobilecloud.video.service;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author satya
 */
@Service
public class VideoService {

    private final VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public Video saveVideoMetadata(Video video){
        return videoRepository.save(video);
    }

    public List<Video> getAllVideos() {
        List<Video>videos = new ArrayList<>();
         videoRepository.findAll().forEach(videos :: add);
         return videos;
    }

    public List<Video> findVideosByName(String name) {
        return videoRepository.findByName(name);
    }

    public List<Video> findVideosByDurationLessThan(long duration) {
        return videoRepository.findByDurationLessThan(duration);
    }


    public Video findVideoById(long id) {
        return videoRepository.findOne(id);
    }

}
