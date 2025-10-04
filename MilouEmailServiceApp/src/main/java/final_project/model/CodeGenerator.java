package final_project.model;

import java.util.Random;

public class CodeGenerator {
    private static final String CHARACTERS = "1234567890abcdefghijklmnopqrstuvwxyz";
    private static final int CODE_LENGTH = 6;
    private static final Random random = new Random();

    public static String generateCode(){
        String code = null;
        while(code == null || !isCodeUnique(code)){
            StringBuilder tempCode = new StringBuilder(CODE_LENGTH);
            for(int i = 0; i < CODE_LENGTH; i++){
                tempCode.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            code = tempCode.toString();
        }
        return code;
    }

    private static boolean isCodeUnique(String code){
        for(Email email : Email.allEmails)
            if(email.getCode().equals(code))
                return false;
        return true;
    }

}
