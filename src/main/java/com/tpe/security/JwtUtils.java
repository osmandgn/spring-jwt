package com.tpe.security;

import com.tpe.security.service.*;
import io.jsonwebtoken.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component //Diğer class'larda da kullanacağımız için Component notasyonunu kullandık.
public class JwtUtils {
    // 1 : JWT generate
    // 2: JWT valide
    // 3 : JWT --> userName // Jwt içerisinden username bilgisini çekeceğiz.
    private String jwtSecret = "sboot"; // secret key'imizi belirledik.

    //Tokenin yaşam süresi milisaniye cinsinden 1 gün olarak ayarlayalım.
    private  long jwtExpirationMs = 86400000;   // 24*60*60*1000

    // !!! ************ GENERATE TOKEN *****************
    //Kullanıcı login kısmını geçti otantike oldu. bu yüzden otantike olan kullanıcının bilgilerini almak için
    //Authentication kullandık parametre olark.
    //token üreteceği için return type String
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        //getPrencipal SecurityContext'e gidip otantike edilen kullanıcının bilgilerini getiriyor.
        // Fakat bu UserDetails türünde geliyor. Cast işlemi yapıp bizim UserDetailsImpl class'ına çeviriyoruz.

        // Artık bir token üretilmesi için bize lazım olan her şey var elimizde.
        //secretkey, expirationtime, ve otantike edilen userdetail.

        return Jwts.builder().
                setSubject(userDetails.getUsername()). //token oluşturulurken kullanılıyor
                setIssuedAt(new Date()). //ne zaman üretildi
                setExpiration(new Date(new Date().getTime() + jwtExpirationMs)).//geçerlilik süresi tarih(oluşturulduğu tarih + geçerlikik süresi)
                signWith(SignatureAlgorithm.HS512, jwtSecret).// şifreleme algoritması + secret key
                compact();//hepsini toparla
    }

    // !!! ****************** VALIDATE TOKEN ***************************

    //parametre olarak token'i alacak ve valide etme durumuna göre boolean değer döndürecek.
    public boolean validateToken(String token){
        //secret key ve token'i veriyoruz. ve gönderdiğimiz token olup olamadığını kontrol ediyoruz.
        //Bu işlem farklı hatalarla karşılaşabilir ve çok farklı exception'lar fırlatabilir.
        //hatanın sebebini görüntülemek için hepsini tek tek catch ediyoruz.
        // code -> sorround with
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false ; //exception alırsa return false olacak.
    }

    // !!! ********** JWT tokenden userName'i alalım ************
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().
                setSigningKey(jwtSecret).//secret key'i verdik
                parseClaimsJws(token).//token'i verdik
                getBody().//body'e git dedik
                getSubject();//yukarıdaki setSubject'in tersine getSubject dedik.
                             // ve username'i aldık.
    }

    //Tüm bu işlemleri jjwt bağımlılığımız sayesinde yaptık.
    //username unique'dir. Bununla o kullanıcının tüm datalarına ulaşmak
    //mümkün olacaktır. O yüzden password yerine username kullandık.

}
