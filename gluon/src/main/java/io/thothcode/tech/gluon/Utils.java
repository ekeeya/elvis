package io.thothcode.tech.gluon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Array;
import java.util.List;

@Slf4j
public class Utils {

    public static <T> String toString(T[] values, char sep) {
        if (values == null) {
            return "";
        }
        String xsep = "";
        StringBuilder sb = new StringBuilder();
        for (T v : values) {
            sb.append(xsep).append(v.toString());
            xsep = sep + "";
        }
        return sb.toString();
    }

    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String && ((String) o).trim().length() == 0) {
            return true;
        }
        if (o instanceof Integer && 0 == (Integer) o) {
            return true;
        }
        if (o.getClass().isArray()) {
            return Array.getLength(o) == 0;
        }

        if (o instanceof List) {
            return ((List<?>) o).isEmpty();
        }
        return o instanceof Long && 0L == (Long) o;
    }

    public static String buildJSON(Object in) {
        ObjectMapper objMapper = new ObjectMapper();
        String objectJson = null;
        try {
            objectJson = objMapper.writeValueAsString(in);
        } catch (JsonProcessingException e) {
            log.warn("warning: ", e);
        }
        return objectJson;
    }

    public static Pageable getPageable(int offset, int count, String orderByField, String order){
        Pageable pageable ;
        if (isEmpty(order) || order.equals("asc")){
            pageable =  PageRequest.of( offset, count, Sort.by(orderByField).ascending());
        }else{
            pageable =  PageRequest.of( offset, count, Sort.by(orderByField).descending());
        }
        return pageable;
    }
}
