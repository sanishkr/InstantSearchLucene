package com.sns.lucene.Controller;

import com.google.gson.JsonObject;
import com.sns.lucene.document.LuceneReadIndex;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NameSearchController {

    @ApiOperation(value = "Suggest upto top 10 names matching keyword", response = Iterable.class)
    @RequestMapping(value = "/SuggestNames", method= RequestMethod.GET,produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    public String Create(@RequestParam String Keyword){
        System.out.println("Keyword:"+Keyword);
        JsonObject res = new JsonObject();
        try {
            if(Keyword.length()<3){
                res.addProperty("Message","Please enter at least 3 characters.");
            }else {
                res = LuceneReadIndex.readIndex("*" + Keyword.toLowerCase() + "*~0.6");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res != null ? res.toString() : null;
    }
}
