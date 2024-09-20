package com.future.getd.net;

import com.future.getd.net.model.GptRequest;
import com.future.getd.net.model.GptResponse;
import com.future.getd.net.model.TranscriptionResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AIChatGptApiService {
    String API_KEY = "sk-proj-LPXdRwo6uvZhqzbYMTyFT3BlbkFJwbszLEjbDnqlwsXXlnhk";
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST(ApiPath.AI_GET_CONTENT_PATH)
//    Observable<MsgAiInfo> getAiMessage(@Query("access_token") String str, @Body AiBody aiBody);
//
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST(ApiPath.AI_GET_TOKEN)
//    Observable<TokenInfoBean> getAiToken(@Query("grant_type") String str, @Query("client_id") String str2, @Query("client_secret") String str3);

    // 2. 使用GPT-4进行文本翻译
    // 接下来，您可以将语音转文本后的结果通过GPT-4 API进行翻译。
    // */
    //func translateText(_ text: String) {
    //    let apiKey = "YOUR_OPENAI_API_KEY"
    //    let url = URL(string: "https://api.openai.com/v1/chat/completions")!
    //
    //    var request = URLRequest(url: url)
    //    request.httpMethod = "POST"
    //    request.setValue("Bearer \(apiKey)", forHTTPHeaderField: "Authorization")
    //    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    //
    //    let parameters: [String: Any] = [
    //        "model": "gpt-4",
    //        "messages": [
    //            ["role": "system", "content": "Translate the following text from English to Chinese:"],
    //            ["role": "user", "content": text]
    //        ]
    //    ]
    //
    //    request.httpBody = try! JSONSerialization.data(withJSONObject: parameters, options: [])
    //
    //    let task = URLSession.shared.dataTask(with: request) { data, response, error in
    //        guard let data = data, error == nil else {
    //            print("Failed to send text for translation: \(error?.localizedDescription ?? "Unknown error")")
    //            return
    //        }
    //
    //        // Parse the response
    //        if let responseJSON = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: Any] {
    //            if let choices = responseJSON["choices"] as? [[String: Any]],
    //               let translatedText = choices.first?["message"] as? [String: Any],
    //               let content = translatedText["content"] as? String {
    //                print("Translated Text: \(content)")
    //                self.speakText(content)
    //            }
    //        }
    //    }
    //    task.resume()
    //}
    @Headers({"Content-Type:application/json","Authorization:Bearer sk-proj-LPXdRwo6uvZhqzbYMTyFT3BlbkFJwbszLEjbDnqlwsXXlnhk"})
    @POST("v1/chat/completions")
    Call<GptResponse> getAiMessage(@Body GptRequest body);

    @Headers({"Content-Type:multipart/form-data","Authorization:Bearer " + API_KEY})
    @Multipart
    @POST("v1/audio/transcriptions")//translations/transcriptions
    Call<TranscriptionResponse> getTranscriptions(@Part MultipartBody.Part filePart/*, @Body GptTranscriptions body*/);

//    @FormUrlEncoded
//    @Headers({"Content-Type:multipart/form-data","Authorization:Bearer " + API_KEY})
//    @POST("v1/audio/transcriptions")//translations/transcriptions
//    Call<GptTranscriptionsResponse> getTranscriptions(@Field("file") File file, @Field("text") String text);

    @Multipart
    @Headers({/*"Content-Type:multipart/form-data",*/"Authorization:Bearer " + API_KEY})
    @POST("v1/audio/transcriptions")
    Call<TranscriptionResponse> transcribeAudio(
            @Part MultipartBody.Part file,
            @Part("model") RequestBody model);
}