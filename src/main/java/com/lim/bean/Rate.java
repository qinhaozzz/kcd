package com.lim.bean;

/**
 * @author qinhao
 */
public class Rate {
    private int send;
    private int receive;
    private int maxSend;
    private int maxReceive;

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public int getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public int getMaxSend() {
        return maxSend;
    }

    public void setMaxSend(int maxSend) {
        this.maxSend = maxSend;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "send=" + send +
                ", receive=" + receive +
                ", maxSend=" + maxSend +
                ", maxReceive=" + maxReceive +
                '}';
    }
}
