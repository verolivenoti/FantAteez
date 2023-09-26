package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.entity.Members;

import java.util.List;

public class FacadeService {

    public static StringBuilder membersList(List<Members> members){
        StringBuilder htmlString = new StringBuilder();
        for(Members member: members){
            htmlString.append("<button class=\"button button5\">" +
                    member.getName() + "\"/></button>");
        }

        return htmlString;
    }
}
