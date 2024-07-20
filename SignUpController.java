package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

import com.example.demo.Dao.SimpleUserDao;


@Controller
public class SignUpController {

    private final SimpleUserDao simpleUserDao;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SignUpController(SimpleUserDao simpleUserDao, BCryptPasswordEncoder passwordEncoder) {
        this.simpleUserDao = simpleUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/sign_up")
    public String signUpForm(Model model) {
        return "sign_up";
    }

    @PostMapping("/sign_up")
    public String signUpFunction(Model model, @RequestParam Map<String, String> params) {
        System.out.println(params); // 값들이 잘 넘어오는지 확인

        String userID = params.get("user_id");
        String userPW = params.get("user_password");
        String checkPW = params.get("user_password_check");
        String realName = params.get("user_realname");

        // Backend validation
        String result = "";
        boolean isValid = true;
        if (userID == null || !userID.endsWith("@naver.com")) {
            result += "User ID should end with '@naver.com'. ";
            isValid = false;
        }
        if (userPW == null || userPW.length() < 4) {
            result += "User password should be at least 4 characters long. ";
            isValid = false;
        }
        if (realName == null || realName.length() <= 0) {
            result += "Real name should be entered. ";
            isValid = false;
        }
        if (!userPW.equals(checkPW)) {
            result += "Password and password confirmation should match. ";
            isValid = false;
        }

        // Submit to database
        if (isValid) {
            try {
                params.remove("_csrf");
                params.put("user_password", passwordEncoder.encode(userPW));
                int rs = simpleUserDao.InsertUserInfo(params);
                if (rs < 1) {
                    result += "Sign up failed. Duplicate information or other problems. ";
                    isValid = false;
                } else {
                    result += "Sign up completed! ";
                }
            } catch (Exception e) {
                e.printStackTrace();
                result += "Sign up failed. Duplicate information or other problems. ";
                isValid = false;
            }
        }

        if (isValid) {
            try {
                int rs = simpleUserDao.InsertAuthorityInfo(userID);
                if (rs < 1) {
                    result += "Sign up failed. Check your information and try again. ";
                    isValid = false;
                } else {
                    result += "Sign up completed! ";
                }
            } catch (Exception e) {
                e.printStackTrace();
                result += "Sign up failed. Check your information and try again. ";
                isValid = false;
            }
        }

        model.addAttribute("isSuccess", isValid);
        model.addAttribute("resultMSG", result);

        return "/sign_up_result";
    }
}
