package com.nilesh.Ecom_Project.controller;

import com.nilesh.Ecom_Project.model.Product;
import com.nilesh.Ecom_Project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;


    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts()
    {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public Product getProductById(@PathVariable int id)
    {
        Product product=service.getProductById(id);
        return product;
//        if(product!=null)
//        {
//            return new ResponseEntity<>(product,HttpStatus.FOUND);
//        }
//        else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }

    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile)
    {
        try
        {
            Product prod=service.addProduct(product,imageFile);
            if(prod!=null)
                return new ResponseEntity<>(prod,HttpStatus.CREATED);
            else
                throw new Exception();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/product/{productid}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productid)
    {
        Product product=service.getProductById(productid);
        byte[] imageFile=product.getImageData();

        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);
    }


    @PutMapping("/product/{productid}")
    public ResponseEntity<String> updateProduct(@PathVariable int productid,@RequestPart Product product, @RequestPart MultipartFile imageFile)
    {
        Product product1=null;
        try {
            product1 = service.updateProduct(productid, product, imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to Update",HttpStatus.BAD_REQUEST);

        }
        if(product1!=null)
            return new ResponseEntity<>("Updated",HttpStatus.OK);

        else
            return new ResponseEntity<>("Failed to Update",HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id)
    {
        Product product1=service.getProductById(id);
        if(product1!=null)
        {
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Product Not found",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword)
    {
        System.out.println("Searching with"+ keyword);
        List<Product>product=service.searchProducts(keyword);

        return new ResponseEntity<>(product,HttpStatus.OK);

    }





}
