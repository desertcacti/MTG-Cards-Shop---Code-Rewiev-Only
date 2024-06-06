package pl.desertcacti.mtgcardsshopsystem.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** ApiResponse class represents a standard structure for API responses,
 * containing information about the success status and a message. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    /** Indicates if the API operation was successful. */
    private boolean success;
    /** Contains a message about the API response. */
    private String message;


   // TO IMPLEMENT
//    /** HTTP status code for the API response. */
//    private int statusCode;
//
//    /** Optional data object for additional information. */
//    private Object data;
}
