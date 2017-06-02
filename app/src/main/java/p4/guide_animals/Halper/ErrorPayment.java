package p4.guide_animals.Halper;

/**
 * Created by kravtsov.a on 02.11.2016.
 */

public final class ErrorPayment {

    public final static String ERROR1 ="contract_not_found";
    public final static String ERROR2 ="not_enough_funds";
    public final static String ERROR3 ="limit_exceeded";
    public final static String ERROR4 ="money_source_not_available";
    public final static String ERROR5 ="illegal_param_csc";
    public final static String ERROR6 ="payment_refused";
    public final static String ERROR7 ="authorization_reject";
    public final static String ERROR8 ="account_blocked";
    public final static String ERROR9 ="illegal_param_ext_auth_success_uri";
    public final static String ERROR10 ="illegal_param_ext_auth_fail_uri";
    public final static String ERROR11 ="ext_action_required";

    public ErrorPayment(){}

    public String getErrorMsg(String error)
    {
        String msg="";
        switch (error)
        {
            case ERROR1:
                msg ="Отсутствует созданный(но не подтвержденный) платеж с заданным request_id, обратитесь к разработчику и укажите ошибку.";
            break;
            case ERROR2:
                msg ="Недостаточно средств на Вашем счете! Необходимо пополнить счет и провести новый платеж.";
                break;
            case ERROR3:
                msg ="Превышен один из лимитов на операции:\n" +
                        "на сумму операции для выданного токена авторизации;\n" +
                        "сумму операции за период времени для выданного токена авторизации;\n" +
                        "ограничений Яндекс.Денег для различных видов операций.";
                break;
            case ERROR4:
                msg ="Запрошенный метод платежа (money_source) недоступен для данного платежа.";
                break;
            case ERROR5:
                msg ="Отсутствует или указано недопустимое значение параметра csc.";
                break;
            case ERROR6:
                msg ="В платеже отказано. Возможные причины:\n" +
                        "магазин отказал в приеме платежа (платёж за товар которого нет в магазине);\n" +
                        "перевод за товар невозможен, превышен лимит остатка кошелька получателя.";
                break;
            case ERROR7:
                msg ="В авторизации платежа отказано. Возможные причины:\n" +
                        "истек срок действия банковской карты;\n" +
                        "банк-эмитент отклонил транзакцию по карте;\n" +
                        "у Вас превышен лимит;\n" +
                        "транзакция с текущими параметрами запрещена;\n" +
                        "Вы не приняли соглашение об использовании сервиса «Яндекс.Деньги».";
                break;
            case ERROR8:
                msg ="Ваш счёт заблокирован. Для разблокировки счета, необходимо перейти в свой аккаунт и разблокировать счёт.";
                break;
            case ERROR9:
                msg ="Отсутствует или указано недопустимое значение параметра ext_auth_success_uri.";
                break;
            case ERROR10:
                msg ="Отсутствует или указано недопустимое значение параметра ext_auth_fail_uri.";
                break;
            case ERROR11:
                msg ="В настоящее время данный тип платежа не может быть проведен. Для получения возможности проведения таких платежей Вам необходимо перейти на страницу своего аккаунта и следовать инструкции на данной странице. Это могут быть следующие действия:\n" +
                        "ввести идентификационные данные,\n" +
                        "принять оферту,\n" +
                        "выполнить иные действия согласно инструкции.";
                break;
            default:
                msg ="В авторизации платежа отказано. Вам следует провести новый платеж спустя некоторое время.";
                break;
        }
       return msg;
    }

}
