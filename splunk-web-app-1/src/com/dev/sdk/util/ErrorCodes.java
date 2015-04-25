package com.dev.sdk.util;


public class ErrorCodes{

    public static String getCodeText(int key) {

        if (key == 100) {
            return "100 - Continue";
        } else if (key == 101) {
            return "101 - Switching Protocols";
        } else if (key == 102) {
            return "102 - Processing";
        } else if (key >= 103 && key <= 199) {
            return "103-199 - Unassigned";
        } else if (key == 200) {
            return "200 - OK";
        } else if (key == 201) {
            return "201 - Created";
        } else if (key == 202) {
            return "202 - Accepted";
        } else if (key == 203) {
            return "203 - Non-Authoritative Information";
        } else if (key == 204) {
            return "204 - No Content";
        } else if (key == 205) {
            return "205 - Reset Content";
        } else if (key == 206) {
            return "206 - Partial Content";
        } else if (key == 207) {
            return "207 - Multi-Status";
        } else if (key == 208) {
            return "208 - Already Reported";
        } else if (key >= 209 && key <= 225) {
            return "209-225 - Unassigned";
        } else if (key == 226) {
            return "226 - IM Used";
        } else if (key >= 227 && key <= 299) {
            return "227-299 - Unassigned";
        } else if (key == 300) {
            return "300 - Multiple Choices";
        } else if (key == 301) {
            return "301 - Moved Permanently";
        } else if (key == 302) {
            return "302 - Found";
        } else if (key == 303) {
            return "303 - See Other";
        } else if (key == 304) {
            return "304 - Not Modified";
        } else if (key == 305) {
            return "305 - Use Proxy";
        } else if (key == 306) {
            return "306 - (Unused)";
        } else if (key == 307) {
            return "307 - Temporary Redirect";
        } else if (key == 308) {
            return "308 - Permanent Redirect";
        } else if (key >= 309 && key <= 399) {
            return "309-399 - Unassigned";
        } else if (key == 400) {
            return "400 - Bad Request";
        } else if (key == 401) {
            return "401 - Unauthorized";
        } else if (key == 402) {
            return "402 - Payment Required";
        } else if (key == 403) {
            return "403 - Forbidden";
        } else if (key == 404) {
            return "404 - Not Found";
        } else if (key == 405) {
            return "405 - Method Not Allowed";
        } else if (key == 406) {
            return "406 - Not Acceptable";
        } else if (key == 407) {
            return "407 - Proxy Authentication Required";
        } else if (key == 408) {
            return "408 - Request Timeout";
        } else if (key == 409) {
            return "409 - Conflict";
        } else if (key == 410) {
            return "410 - Gone";
        } else if (key == 411) {
            return "411 - Length Required";
        } else if (key == 412) {
            return "412 - Precondition Failed";
        } else if (key == 413) {
            return "413 - Payload Too Large";
        } else if (key == 414) {
            return "414 - URI Too Long";
        } else if (key == 415) {
            return "415 - Unsupported Media Type";
        } else if (key == 416) {
            return "416 - Range Not Satisfiable";
        } else if (key == 417) {
            return "417 - Expectation Failed";
        } else if (key >= 418 && key <= 420) {
            return "418-420 - Unassigned";
        } else if (key == 421) {
            return "421 - Misdirected Request";
        } else if (key == 422) {
            return "422 - Unprocessable Entity";
        } else if (key == 423) {
            return "423 - Locked";
        } else if (key == 424) {
            return "424 - Failed Dependency";
        } else if (key == 425) {
            return "425 - Unassigned";
        } else if (key == 426) {
            return "426 - Upgrade Required";
        } else if (key == 427) {
            return "427 - Unassigned";
        } else if (key == 428) {
            return "428 - Precondition Required";
        } else if (key == 429) {
            return "429 - Too Many Requests";
        } else if (key == 430) {
            return "430 - Unassigned";
        } else if (key == 431) {
            return "431 - Request Header Fields Too Large";
        } else if (key >= 432 && key <= 499) {
            return "432-499 - Unassigned";
        } else if (key == 500) {
            return "500 - Internal Server Error";
        } else if (key == 501) {
            return "501 - Not Implemented";
        } else if (key == 502) {
            return "502 - Bad Gateway";
        } else if (key == 503) {
            return "503 - Service Unavailable";
        } else if (key == 504) {
            return "504 - Gateway Timeout";
        } else if (key == 505) {
            return "505 - HTTP Version Not Supported";
        } else if (key == 506) {
            return "506 - Variant Also Negotiates";
        } else if (key == 507) {
            return "507 - Insufficient Storage";
        } else if (key == 508) {
            return "508 - Loop Detected";
        } else if (key == 509) {
            return "509 - Unassigned";
        } else if (key == 510) {
            return "510 - Not Extended";
        } else if (key == 511) {
            return "511 - Network Authentication Required";
        } else if (key >= 512 && key <= 599) {
            return "512-599 - Unassigned";
        } else {
            return "NONE";
        }
    }

}
