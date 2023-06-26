package searchengine.controllers;

import searchengine.services.IndexingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class IndexingController {

    private final IndexingService index;

    public IndexingController(IndexingService index) {
        this.index = index;
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<Object> startIndexingAll() {
        return ResponseEntity.ok(index.startIndexingAll());
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<Object> stopIndexingAll() {
        return ResponseEntity.ok(index.stopIndexing());
    }

    @PostMapping("/indexPage")
    public ResponseEntity<Object> startIndexingOne(
            @RequestParam(name="url", required=false, defaultValue=" ") String url) {
        return ResponseEntity.ok(index.startIndexingOne(url));
    }
}
