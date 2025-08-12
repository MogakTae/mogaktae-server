package code.mogaktae.global.security.jwt;

import code.mogaktae.global.exception.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch (JwtException e){
            setErrorResponse(response, e);
        }
    }

    protected void setErrorResponse(HttpServletResponse response, JwtException e) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse<String> errorResponse = new ErrorResponse<>(
                HttpStatus.UNAUTHORIZED.toString(),
                e.getMessage()
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
