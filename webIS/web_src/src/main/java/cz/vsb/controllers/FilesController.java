package cz.vsb.controllers;

import cz.vsb.application.files.FilesLoader;

import cz.vsb.application.files.PropertyLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class FilesController {

    @RequestMapping("/update_files")
    @CrossOrigin(origins = "*")
    public String updateFiles(HttpServletRequest request){
        if(request.getRemoteAddr().equals(PropertyLoader.loadProperty("fileUpdaterIP"))){
            FilesLoader.reloadFiles();
            return "New files were loaded.";
        }
        return "you are not authorized for this operation!";
    }
}


