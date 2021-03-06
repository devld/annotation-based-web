package ld.web.request;

import ld.common.context.Context;
import ld.common.request.RequestMapping;
import ld.common.request.param.PathVariable;
import ld.common.request.param.RequestParam;
import ld.common.response.Model;

@RequestMapping("/")
public class Index {

    @RequestMapping("/hello/{id}")
    public String hello(Model model,
                        Context context,
                        @PathVariable("id") int id,
                        @RequestParam("hello") String name) {
        model.put("hello", "Greeting from ...");
        model.put("id", id);
        model.put("name", name);
        return "index";
    }

}
