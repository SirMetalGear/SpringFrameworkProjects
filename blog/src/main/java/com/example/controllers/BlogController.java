package com.example.controllers;

import com.example.models.Post;
import com.example.repo.PostRepository;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title,
                              @RequestParam String anons,
                              @RequestParam String fullText,
                              Model model) {
        Post post = new Post(title, anons, fullText);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable("id") long id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null)
            return "redirect:/blog";
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
        model.addAttribute("post", post);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable("id") long id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null)
            return "redirect:/blog";
        model.addAttribute("post", post);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostEdit(@PathVariable("id") long id,
                               @RequestParam String title,
                               @RequestParam String anons,
                               @RequestParam String fullText,
                               Model model) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null)
            return "redirect:/blog";
        post.setTitle(title);
        post.setAnons(anons);
        post.setFullText(fullText);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable("id") long id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null)
            return "redirect:/blog";
        postRepository.delete(post);
        return "redirect:/blog";
    }

}
