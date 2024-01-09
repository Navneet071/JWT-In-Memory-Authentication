package com.jwt.example.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


// Create JWTAuthenticationFilter that extends OncePerRequestFilter and override method and write the logic to check the token that is comming in header. We have to write 5 important logic

// 1. Get Token from request
// 2. Validate Token
// 3. GetUsername from token
// 4. Load user associated with this token
// 5. Set Authentication

/* 

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
//     @Autowired
//     private JwtHelper jwtHelper;


//     @Autowired
//     private UserDetailsService userDetailsService;
    
//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {
                
//             // Authorization = Bearer dgfhf....W$#$dfh
//             String requestHeader = request.getHeader("Authorization");
//             logger.info("Header : {}", requestHeader);
                
//             String userName = null;
//             String token = null;

//             if(requestHeader != null && requestHeader.startsWith("Bearer")){
//                 // here all good
//                 token = requestHeader.substring(7);

//                 try{
//                     userName = this.jwtHelper.getUsernameFromToken(token);

//                 }
//                 catch(IllegalArgumentException e){
//                     logger.info("Illegal Argument while fetching the username !!");
//                     e.printStackTrace();
//                 }
//                 catch(ExpiredJwtException e){
//                     logger.info("Given JWT token has been expired");
//                     e.printStackTrace();
//                 }
//                 catch(MalformedJwtException e){
//                     logger.info("Some changed has done in token !! Invalid Token");
//                     e.printStackTrace();
//                 }
//                 catch(Exception e){
//                     logger.info("show error");
//                     e.printStackTrace();
//                 }
//             }else{
//                 logger.info("!!! Invalid Header Value !!!");
//             }


//             /// after getting username now we will match it WITH THE DATABASE and check it exist all or not

//             if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//                 // fetch userdetails from the username
//                 UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
//                 Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
//                 if (validateToken) {

//                     // lets set the Authentication
//                     UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, validateToken);
//                     authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                     SecurityContextHolder.getContext().setAuthentication(authentication);

//                     // authentication is done now
//                 }
//                 else{
//                     logger.info("Validation get Failed");
//                 }

//             }

//             filterChain.doFilter(request, response);




            


//         throw new UnsupportedOperationException("Unimplemented method 'doFilterInternal'");
//     }
    
// }


*/


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    @Autowired
    private JwtHelper jwtHelper;


    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        //Authorization

        String requestHeader = request.getHeader("Authorization");
        //Bearer 2352345235sdfrsfgsdfsdf
        logger.info(" Header :  {}", requestHeader);
        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            //looking good
            token = requestHeader.substring(7);
            try {

                username = this.jwtHelper.getUsernameFromToken(token);

            } catch (IllegalArgumentException e) {
                logger.info("Illegal Argument while fetching the username !!");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                logger.info("Given jwt token is expired !!");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                logger.info("Some changed has done in token !! Invalid Token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }


        } else {
            logger.info("Invalid Header Value !! ");
        }


        //
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {


            //fetch user detail from username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
            if (validateToken) {

                //set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);


            } else {
                logger.info("Validation fails !!");
            }


        }

        filterChain.doFilter(request, response);


    }
}
