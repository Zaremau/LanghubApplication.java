package com.zarema.langhub.controller;

import com.zarema.langhub.model.Post;
import com.zarema.langhub.model.Users;
import com.zarema.langhub.service.FileService;
import com.zarema.langhub.service.PostService;
import com.zarema.langhub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/posts/{id}")
    public ResponseEntity<Model> getPost(@PathVariable Integer id, Model model) {
        return new ResponseEntity<>(postService.getPost(id, model).get(), HttpStatus.OK);
    }

    @PostMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updatePost(@PathVariable Integer id, Post post, @RequestParam("file") MultipartFile file) {
        try {
            return new ResponseEntity<>(postService.updatePost(id, post, file), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createNewPost(Model model) {
        return new ResponseEntity<>(postService.getCreateNewPost(model), HttpStatus.OK);
    }

    @PostMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createNewPost(@ModelAttribute Post post, @RequestParam("file") MultipartFile file, Principal principal) {
        return new ResponseEntity<>(postService.createNewPost(post, file, principal), HttpStatus.OK);
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getPostForEdit(@PathVariable Integer id, Model model) {
        return new ResponseEntity<>(postService.getPostForEdit(id, model), HttpStatus.OK);
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deletePost(@PathVariable Integer id) {
        return new ResponseEntity<>(postService.deletePost(id), HttpStatus.OK);
    }

}
