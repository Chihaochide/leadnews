// This file is auto-generated, don't edit it. Thanks.
package com.liuxuanhe.common.aliyun;

import com.aliyun.imageaudit20191230.models.ScanTextRequest;
import com.aliyun.imageaudit20191230.models.ScanTextResponse;
import com.aliyun.imageaudit20191230.models.ScanTextResponseBody;
import com.aliyun.tea.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Sample {

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.imageaudit20191230.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "imageaudit.cn-shanghai.aliyuncs.com";
        return new com.aliyun.imageaudit20191230.Client(config);
    }
    public static List<ScanTextRequest.ScanTextRequestTasks> getTasksList(List<String> list){
        ArrayList<ScanTextRequest.ScanTextRequestTasks> result = new ArrayList<>();
        for (Object o : list) {
            result.add(new ScanTextRequest.ScanTextRequestTasks().setContent((String) o));
        }
        return result;
    }
    public static List<ScanTextRequest.ScanTextRequestLabels> getLabels(){
        List<ScanTextRequest.ScanTextRequestLabels> list = new ArrayList<>();
        com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels labels0 = new com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels()
                .setLabel("spam");
        com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels labels1 = new com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels()
                .setLabel("politics");
        com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels labels2 = new com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels()
                .setLabel("abuse");
        com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels labels3 = new com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels()
                .setLabel("terrorism");
        com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels labels4 = new com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels()
                .setLabel("porn");
        com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels labels5 = new com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels()
                .setLabel("flood");
        com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels labels6 = new com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels()
                .setLabel("contraband");
        com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels labels7 = new com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestLabels()
                .setLabel("ad");
        Collections.addAll(list,labels0,labels1,labels2,labels3,labels4,labels5,labels6,labels7);
        return list;

    }
    public static List<String> getSuggestion(ScanTextResponse response){
        ScanTextResponseBody.ScanTextResponseBodyData data = response.getBody().getData();
        Map<String, Object> map = data.toMap();
        System.out.println("map = " + map);
        List<Map<String,Object>> list = (ArrayList) map.get("Elements");
        ArrayList<String> result = new ArrayList<>();
        for (Map o : list) {
            List<Map<String,Object>> ls = (ArrayList)o.get("Results");
            for (Map<String, Object> l : ls) {
                result.add((String) l.get("Suggestion"));
            }
        }
        System.out.println("result = " + result);
        return result;
    }

//    public static void main(String[] args_) throws Exception {
//        java.util.List<String> args = java.util.Arrays.asList(args_);
//        // 工程代码泄露可能会导致AccessKey泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
//        com.aliyun.imageaudit20191230.Client client = Sample.createClient("accessKeyId", "accessKeySecret");
//
//        com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestTasks tasks0 = new com.aliyun.imageaudit20191230.models.ScanTextRequest.ScanTextRequestTasks()
//                .setContent("本校小额贷款，安全、快捷、方便、无抵押，随机随贷，当天放款，上门服务。联系weixin 123456");
//        com.aliyun.imageaudit20191230.models.ScanTextRequest scanTextRequest = new com.aliyun.imageaudit20191230.models.ScanTextRequest()
//                .setTasks(java.util.Arrays.asList(
//                    tasks0
//                ))
//                .setLabels(getLabels());
//        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
//        try {
//            // 复制代码运行请自行打印 API 的返回值
//            client.scanTextWithOptions(scanTextRequest, runtime);
//        } catch (TeaException error) {
//            // 如有需要，请打印 error
//            com.aliyun.teautil.Common.assertAsString(error.message);
//        } catch (Exception _error) {
//            TeaException error = new TeaException(_error.getMessage(), _error);
//            // 如有需要，请打印 error
//            com.aliyun.teautil.Common.assertAsString(error.message);
//        }
//    }
}