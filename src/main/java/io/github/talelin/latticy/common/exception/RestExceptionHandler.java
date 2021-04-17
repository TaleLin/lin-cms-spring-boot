package io.github.talelin.latticy.common.exception;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.exception.HttpException;
import io.github.talelin.latticy.common.configuration.CodeMessageConfiguration;
import io.github.talelin.latticy.vo.UnifyResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.talelin.autoconfigure.util.RequestUtil.getSimpleRequest;

/**
 * @author pedro@TaleLin
 * @author colorful@TaleLin
 * @author Juzi@TaleLin
 */
@Order
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size:20M}")
    private String maxFileSize;

    /**
     * HttpException
     */
    @ExceptionHandler({HttpException.class})
    public UnifyResponseVO processException(HttpException exception, HttpServletRequest request, HttpServletResponse response) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        UnifyResponseVO unifyResponse = new UnifyResponseVO();
        unifyResponse.setRequest(getSimpleRequest(request));
        int code = exception.getCode();
        boolean defaultMessage = exception.ifDefaultMessage();
        unifyResponse.setCode(code);
        response.setStatus(exception.getHttpCode());
        String errorMessage = CodeMessageConfiguration.getMessage(code);
        if (!StringUtils.hasText(errorMessage) || !defaultMessage) {
            unifyResponse.setMessage(exception.getMessage());
            log.error("", exception);
        } else {
            unifyResponse.setMessage(errorMessage);
            log.error("", exception.getClass().getConstructor(int.class, String.class).newInstance(code, errorMessage));
        }
        return unifyResponse;
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public UnifyResponseVO processException(ConstraintViolationException exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("", exception);
        Map<String, Object> msg = new HashMap<>();
        exception.getConstraintViolations().forEach(constraintViolation -> {
            String template = constraintViolation.getMessage();
            String path = constraintViolation.getPropertyPath().toString();
            msg.put(com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(path), template);
        });
        UnifyResponseVO unifyResponse = new UnifyResponseVO();
        unifyResponse.setRequest(getSimpleRequest(request));
        unifyResponse.setMessage(msg);
        unifyResponse.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return unifyResponse;
    }

    /**
     * NoHandlerFoundException
     */
    @ExceptionHandler({NoHandlerFoundException.class})
    public UnifyResponseVO processException(NoHandlerFoundException exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("", exception);
        UnifyResponseVO unifyResponse = new UnifyResponseVO();
        unifyResponse.setRequest(getSimpleRequest(request));
        String message = CodeMessageConfiguration.getMessage(10025);
        if (!StringUtils.hasText(message)) {
            unifyResponse.setMessage(exception.getMessage());
        } else {
            unifyResponse.setMessage(message);
        }
        unifyResponse.setCode(Code.NOT_FOUND.getCode());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return unifyResponse;
    }

    /**
     * MissingServletRequestParameterException
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public UnifyResponseVO processException(MissingServletRequestParameterException exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("", exception);
        UnifyResponseVO result = new UnifyResponseVO();
        result.setRequest(getSimpleRequest(request));

        String errorMessage = CodeMessageConfiguration.getMessage(10150);
        if (!StringUtils.hasText(errorMessage)) {
            result.setMessage(exception.getMessage());
        } else {
            result.setMessage(errorMessage + exception.getParameterName());
        }
        result.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return result;
    }

    /**
     * MethodArgumentTypeMismatchException
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public UnifyResponseVO processException(MethodArgumentTypeMismatchException exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("", exception);
        UnifyResponseVO result = new UnifyResponseVO();
        result.setRequest(getSimpleRequest(request));
        String errorMessage = CodeMessageConfiguration.getMessage(10160);
        if (!StringUtils.hasText(errorMessage)) {
            result.setMessage(exception.getMessage());
        } else {
            result.setMessage(exception.getValue() + errorMessage);
        }
        result.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return result;
    }

    /**
     * ServletException
     */
    @ExceptionHandler({ServletException.class})
    public UnifyResponseVO processException(ServletException exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("", exception);
        UnifyResponseVO result = new UnifyResponseVO();
        result.setRequest(getSimpleRequest(request));
        result.setMessage(exception.getMessage());
        result.setCode(Code.FAIL.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return result;
    }

    /**
     * MethodArgumentNotValidException
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public UnifyResponseVO processException(
            MethodArgumentNotValidException exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("", exception);
        BindingResult bindingResult = exception.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();
        Map<String, Object> msg = new HashMap<>();
        errors.forEach(error -> {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                msg.put(com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(fieldError.getField()),
                        fieldError.getDefaultMessage());
            } else {
                msg.put(com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(error.getObjectName()), error.getDefaultMessage());
            }
        });
        UnifyResponseVO result = new UnifyResponseVO();
        result.setRequest(getSimpleRequest(request));
        result.setMessage(msg);
        result.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return result;
    }

    /**
     * HttpMessageNotReadableException
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public UnifyResponseVO processException(HttpMessageNotReadableException exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("", exception);
        UnifyResponseVO result = new UnifyResponseVO();
        result.setRequest(getSimpleRequest(request));
        String errorMessage = CodeMessageConfiguration.getMessage(10170);
        Throwable cause = exception.getCause();
        if(cause != null) {
            String msg = this.convertMessage(cause);
            result.setMessage(msg);
        } else {
            if (!StringUtils.hasText(errorMessage)) {
                result.setMessage(exception.getMessage());
            } else {
                result.setMessage(errorMessage);
            }
        }
        result.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return result;
    }

    /**
     * TypeMismatchException
     */
    @ExceptionHandler({TypeMismatchException.class})
    public UnifyResponseVO processException(TypeMismatchException exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("", exception);
        UnifyResponseVO result = new UnifyResponseVO();
        result.setRequest(getSimpleRequest(request));
        result.setMessage(exception.getMessage());
        result.setCode(Code.PARAMETER_ERROR.getCode());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return result;
    }

    /**
     * MaxUploadSizeExceededException
     */
    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public UnifyResponseVO processException(MaxUploadSizeExceededException exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("", exception);
        UnifyResponseVO result = new UnifyResponseVO();
        result.setRequest(getSimpleRequest(request));
        String errorMessage = CodeMessageConfiguration.getMessage(10180);
        if (!StringUtils.hasText(errorMessage)) {
            result.setMessage(exception.getMessage());
        } else {
            result.setMessage(errorMessage + maxFileSize);
        }
        result.setCode(Code.FILE_TOO_LARGE.getCode());
        response.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
        return result;
    }

    /**
     * Exception
     */
    @ExceptionHandler({Exception.class})
    public UnifyResponseVO processException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("", exception);
        UnifyResponseVO result = new UnifyResponseVO();
        result.setRequest(getSimpleRequest(request));
        result.setMessage(Code.INTERNAL_SERVER_ERROR.getZhDescription());
        result.setCode(Code.INTERNAL_SERVER_ERROR.getCode());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return result;
    }

    /**
     * 传参类型错误时，用于消息转换
     * @param throwable
     * @return 错误信息
     */
    private String convertMessage(Throwable throwable) {
        String error = throwable.toString();
        String regulation = "\\[\"(.*?)\"]+";
        Pattern pattern = Pattern.compile(regulation);
        Matcher matcher = pattern.matcher(error);
        String group = "";
        if(matcher.find()) {
            String matchString = matcher.group();
            matchString = matchString
                    .replace("[","")
                    .replace("]","");
            matchString = matchString.replaceAll("\\\"","") + "字段类型错误";
            group += matchString;
        }
        return group;
    }
}
