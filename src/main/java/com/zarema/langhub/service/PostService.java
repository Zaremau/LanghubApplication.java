package com.zarema.langhub.service;


import com.zarema.langhub.model.Post;
import com.zarema.langhub.model.Users;
import com.zarema.langhub.repo.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileService fileService;
    private final UserService userService;

    @Autowired
    public PostService(FileService fileService, PostRepository postRepository, UserService userService) {
        this.fileService = fileService;
        this.postRepository = postRepository;
        this.userService= userService;
    }

    public Optional<Model> getPost(Integer id, Model model) {

        // find post by id
        Optional<Post> optionalPost = postRepository.findById(id);

        // if post exists put it in model
        Post post = optionalPost.get();
        return Optional.ofNullable(Optional.of(model.addAttribute("post", post)).orElseThrow(() -> new NoSuchElementException("No such post")));

    }
    public String updatePost(Integer id, Post post, MultipartFile file) throws Exception {

        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());

            try {
                fileService.save(file);
                existingPost.setImageFilePath(file.getOriginalFilename());
            } catch (Exception e) {
               throw new Exception();
            }

            if (existingPost.getId() == null) {
                existingPost.setCreatedAt(LocalDateTime.now());
            }
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(existingPost);
        }

        return "redirect:/posts/" + post.getId();
    }

    public String getCreateNewPost(Model model) {

        Post post = new Post();
        model.addAttribute("post", post);
        return "post_new";
    }

    public String createNewPost(Post post, MultipartFile file, Principal principal) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Users user = userService.findPrinciple(principal).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        try {
            fileService.save(file);
            post.setImageFilePath(file.getOriginalFilename());
        } catch (Exception e) {}

        post.setUser(user);
        if (post.getId() == null) {
            post.setCreatedAt(LocalDateTime.now());
        }
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
        return "redirect:/";
    }
    public String getPostForEdit(Integer id, Model model) {

        // find post by id
        Optional<Post> optionalPost = postRepository.findById(id);
        // if post exist put it in model
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "404";
        }
    }
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public String deletePost(@PathVariable Integer id) {

        // find post by id
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            postRepository.delete(post);
            return "redirect:/";
        } else {
            return "404";
        }
    }

}


