/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author matti
 */
public class RecaptchaDTO {
    boolean success;
    String challenge_ts;

    public RecaptchaDTO(boolean success, String challenge_ts) {
        this.success = success;
        this.challenge_ts = challenge_ts;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getChallenge_ts() {
        return challenge_ts;
    }

    public void setChallenge_ts(String challenge_ts) {
        this.challenge_ts = challenge_ts;
    }
    
}
