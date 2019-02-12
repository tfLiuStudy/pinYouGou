package cn.itcast.core.controller.upload;

import cn.itcast.core.entity.Result;
import cn.itcast.core.utils.fdfs.FastDFSClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_PATH}")
    private String FILE_SERVER_PATH;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @RequestMapping("/uploadFile.do")
    public Result upload(MultipartFile file){

        try {
            String conf="classpath:fdfs/fdfs_client.conf";
//            创建一个文件系统客户端对象
            FastDFSClient fastDFSClient = new FastDFSClient(conf);
//            获取文件名
            String originalFilename = file.getOriginalFilename();

            String extname = FilenameUtils.getExtension(originalFilename);

            String path = fastDFSClient.uploadFile(file.getBytes(), extname, null);

            System.out.println(path);

            path=FILE_SERVER_PATH+path;

            return new Result(true,path);

        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false,"上传失败");
        }

    }


}
