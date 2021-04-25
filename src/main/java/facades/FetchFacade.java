package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utils.HttpUtils;

public class FetchFacade {

    class Default implements Callable<String> {

        String url;

        Default(String url) {
            this.url = url;
        }

        @Override
        public String call() throws Exception {
            String raw = HttpUtils.fetchData(url);
            return raw;
        }
    }

    public List<String> fetchParallel() throws InterruptedException, ExecutionException {
        String[] hostList = {"https://api.chucknorris.io/jokes/random", "https://icanhazdadjoke.com",
            "https://swapi.dev/api/planets/schema", "https://swapi.dev/api/vehicles/schema", "https://swapi.dev/api/species/schema"};
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<String>> futures = new ArrayList<>();
        List<String> retList = new ArrayList();

        for (String url : hostList) {
            Callable<String> urlTask = new Default(url);
            Future future = executor.submit(urlTask);
            futures.add(future);
        }

        for (Future<String> fut : futures) {
            retList.add(fut.get());
        }
       
        return retList;
    }
    public void saveToFile(InputStream uploadedInputStream,
        String uploadedFileLocation) {

    try {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];

        out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    } catch (IOException e) {

        e.printStackTrace();
    }

}
    
}
