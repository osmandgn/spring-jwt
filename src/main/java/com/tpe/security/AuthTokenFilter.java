package com.tpe.security;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.util.*;
import org.springframework.web.filter.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

// Bu class'ta User ve Password Validation İşlemlerimizi
// Ve JWT Generate ve Validation işlemlerimizi (JwtUtils üzeirnden) yapmamız gerekiyor.

//Bu class'ın filter olduğunu bildirmek için Spring'in oluşturudğu class'a extends ediyorum.
public class AuthTokenFilter extends OncePerRequestFilter {

    // Kullancağım objeleri için enjekte ediyorum.

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    // extends ettiğim için kendi gelen metodu Override ediyorum.
    @Override
    protected void doFilterInternal(HttpServletRequest request, //bize gelen request'e erişiyoruz.
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = parseJwt(request); // Aşağıdaki metodu kullanarak request'ten tokeni aldık.

        //Jwt token null değilse ve benim gönderdiğim jwttoken ise
        // (JwtUtils'teki validate metodumla kontrol ediyorum)
        try {
            if(jwtToken!=null && jwtUtils.validateToken(jwtToken)){

                String userName  = jwtUtils.getUserNameFromJwtToken(jwtToken); // username bilgisine ulaşabilirz
                //username'i securitycontext'e atabilmek için alıyoruz ki bunun üzerinden tüm bilgilere ulaşabilelim.
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                //username ile  userDetails i aldık hazır bir metod olan loadUserByUsername i kullanarak.

                //Otantike edilmiş bir user elde etmek için aşağıdaki işlemleri yapıyoruz.
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(//üç parametre alıyor.
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                //Artık username ve password bilgileri ile otantike edilmiş bir user var elimizde.
                // daha sonra user'ımızı SecurityContext'e ekliyoruz.
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request,response);
        // Hem request hem de response için oluşturduğumuz bu filter'ı filterchain'e ekliyoruz.


    }

    //request ile birlikte gelen token'i alıyoruz.
    //Bu token Header kısmında, key value şeklinde gelir.
    //Key Authorization kelimesidir. Value yani token Bearer ile başlar.

    private String parseJwt(HttpServletRequest request){ // request'e ulaştık.
        String header =  request.getHeader("Authorization"); // key'i kullanarak value'ye ulaştık.
        // header value'su text içeriyor mu ve Bearer ile başlıyor mu.
        if(StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);//Bearer 'den sonrasını aldık. Ve Token'i return ettik.
        }
        return null;

    }

    @Override // Filtrelenmesini istemediğimiz entpoint'leri buraya ekliyoruz.
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match("/register", request.getServletPath()) ||
                antPathMatcher.match("/login" , request.getServletPath());
    }
}
