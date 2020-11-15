package com.hyperwallet.clientsdk.util;

    import org.testng.annotations.Test;
    import java.util.*;

    import static org.hamcrest.MatcherAssert.assertThat;
    import static org.hamcrest.Matchers.*;

    public class MultipartTest {

        @Test
        public void testFields() {
            Multipart multipart = new Multipart();
            Multipart.MultipartData data = new Multipart.MultipartData(null, null, null);

            assertThat(data.getContentDisposition(), is(nullValue()));
            assertThat(data.getContentType(), is(nullValue()));
            assertThat(data.getEntity(), is(nullValue()));
            assertThat(data.getEntity(), is(nullValue()));
            Map<String, String> entity = new HashMap<String, String>();
            entity.put("drivers_license_front", "/path/to/test/file");
            data = new Multipart.MultipartData("Content-type: json", "Content-Disposition: form-data", entity);
            assertThat(data.getContentDisposition(), is(equalTo("Content-Disposition: form-data")));
            assertThat(data.getContentType(), is(equalTo("Content-type: json")));
            assertThat(data.getEntity().get("drivers_license_front"), is(equalTo("/path/to/test/file")));

            List<Multipart.MultipartData> dataList = new ArrayList<Multipart.MultipartData>();
            dataList.add(data);
            multipart.setMultipartList(dataList);

        }
    }


