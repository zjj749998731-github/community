package life.majiang.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import life.majiang.community.exception.MyException;
import life.majiang.community.exception.MyExceptionCodeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class UCloudProvider {

    @Value("${ucloud.ufile.public_key}")
    private String publicKey;

    @Value("${ucloud.ufile.private_key}")
    private String privateKey;

    @Value("${ucloud.ufile.bucket_name}")
    private String bucketName;

    @Value("${ucloud.ufile.region}")
    private String region;

    @Value("${ucloud.ufile.proxy_suffix}")
    private String proxySuffix;

    @Value("${ucloud.ufile.expires_duration}")
    private Integer expiresDuration;


    public String upload(InputStream fileInputStream, String mimeType,String fileName){
        // 对象相关API的授权器
        ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(publicKey, privateKey);

        // 对象操作需要ObjectConfig来配置您的地区和域名后缀  zjj.cn-bj.ufileos.com
        ObjectConfig config = new ObjectConfig(region, proxySuffix);

        //修改从页面传进来的图片名字，避免上传至UCloud图片的名字重复
        String uploadFileName = "";
        if (fileName != null){
            String[] fileNameSplit = fileName.split("//.");
            uploadFileName = UUID.randomUUID().toString() + "." + fileNameSplit[fileNameSplit.length - 1];  // xxx.扩展名
        }else {
            throw new MyException(MyExceptionCodeEnum.FILE_UPLOAD_FAIL);
        }

        //同步上传图片到UCloud，即UCloud保存该图片
        try {
            PutObjectResultBean response = UfileClient.object(objectAuthorization, config)
                    .putObject(fileInputStream, mimeType)
                    .nameAs(uploadFileName)
                    .toBucket(bucketName)
                    /**
                     * 是否上传校验MD5, Default = true
                     */
                    //  .withVerifyMd5(false)
                    /**
                     * 指定progress callback的间隔, Default = 每秒回调
                     */
                    //  .withProgressConfig(ProgressConfig.callbackWithPercent(10))
                    /**
                     * 配置进度监听
                     */
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(long bytesWritten, long contentLength) {

                        }
                    })
                    .execute();
            if (response != null && response.getRetCode() == 0){
                 String url = UfileClient.object(objectAuthorization, config)
                         .getDownloadUrlFromPrivateBucket(uploadFileName, bucketName, expiresDuration)
                         .createUrl();
                 return url;
             } else {
                throw new MyException(MyExceptionCodeEnum.FILE_UPLOAD_FAIL);
            }
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new MyException(MyExceptionCodeEnum.FILE_UPLOAD_FAIL);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new MyException(MyExceptionCodeEnum.FILE_UPLOAD_FAIL);
        }

    }



}
