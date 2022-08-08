package fpt.capstone.form_data;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class idForm {
    private String id = "";



    public idForm() {
    }



    public idForm(String id) {
        this.id = id;
    }



    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
