/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video.controller;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import retrofit.http.Query;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author satya
 */
@Controller
public class VideoController {

	private final VideoService videoService;

	@Autowired
	public VideoController(VideoService videoService){
		this.videoService = videoService;
	}

	@RequestMapping(value="/go",method=RequestMethod.GET)
	public @ResponseBody String goodLuck(){
		return "Good Luck!";
	}

	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v) {
		return videoService.saveVideoMetadata(v);
	}

	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<Video> getVideoList() {
		return videoService.getAllVideos();
	}

	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH + "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Video getVideoById(@PathVariable("id") long id) {
		return videoService.findVideoById(id);
	}



	@RequestMapping(value=VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<Video> getVideoByName(@Query("title") String title) {
		return videoService.findVideosByName(title);
	}

	@RequestMapping(value=VideoSvcApi.VIDEO_DURATION_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<Video> getVideoByDurationLessThan(@Query("duration") long duration){
		return videoService.findVideosByDurationLessThan(duration);
	}

	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method = RequestMethod.POST)
	public void likeVideo(@PathVariable("id") Long id, Principal p, HttpServletResponse response) throws IOException {
		String username = p.getName();
		Video video = videoService.findVideoById(id);

		if (video==null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String userLike = video.getLikedBy().stream().filter(user -> user.equals(username)).findFirst().orElse(null);
		if (userLike != null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Already like this Video");
		}
		else {
			Set<String> likedBy = new HashSet<String>();
			likedBy.add(username);
			video.setLikedBy(likedBy);
			video.setLikes(likedBy.size());
			videoService.saveVideoMetadata(video);
		}

	}

	@RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike", method = RequestMethod.POST)
	public void unLikeVideo(@PathVariable("id") Long id, Principal p, HttpServletResponse response) throws IOException {
		String username = p.getName();
		Video video = videoService.findVideoById(id);

		if (null == video) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		Set<String> likedBy= video.getLikedBy().stream().filter(user -> !user.equals(username)).collect(Collectors.toSet());
			video.setLikedBy(likedBy);
			video.setLikes(likedBy.size());
			videoService.saveVideoMetadata(video);


	}








}
