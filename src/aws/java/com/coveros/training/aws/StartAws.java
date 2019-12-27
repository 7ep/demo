package com.coveros.training.aws;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.CancelSpotInstanceRequestsRequest;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsRequest;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsResult;
import com.amazonaws.services.ec2.model.LaunchSpecification;
import com.amazonaws.services.ec2.model.RequestSpotInstancesRequest;
import com.amazonaws.services.ec2.model.RequestSpotInstancesResult;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import org.junit.Test;

/**
 * Welcome to your new AWS Java SDK based project!
 *
 * This class is meant as a starting point for your console-based application that
 * makes one or more calls to the AWS services supported by the Java SDK, such as EC2,
 * SimpleDB, and S3.
 *
 * In order to use the services in this sample, you need:
 *
 *  - A valid Amazon Web Services account. You can register for AWS at:
 *       https://aws-portal.amazon.com/gp/aws/developer/registration/index.html
 *
 *  - Your account's Access Key ID and Secret Access Key:
 *       http://aws.amazon.com/security-credentials
 *
 *  - A subscription to Amazon EC2. You can sign up for EC2 at:
 *       http://aws.amazon.com/ec2/
 *
 */

public class StartAws {

    /*
     * Before running the code:
     *      Fill in your AWS access credentials in the provided credentials
     *      file template, and be sure to move the file to the default location
     *      (~/.aws/credentials) where the sample code will load the
     *      credentials from.
     *      https://console.aws.amazon.com/iam/home?#security_credential
     *
     * WARNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    @Test
    public void shouldSubmitRequest() {
        //============================================================================================//
        //=============================== Submitting a Request =======================================//
        //============================================================================================//

        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (~/.aws/credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        // Create the AmazonEC2Client object so we can call various APIs.
        AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-2")
                .build();

        // Initializes a Spot Instance Request
        RequestSpotInstancesRequest requestRequest = new RequestSpotInstancesRequest();

        // Request 1 x t1.micro instance with a bid price of $0.03.
        requestRequest.setSpotPrice("0.03");
        requestRequest.setInstanceCount(Integer.valueOf(1));

        // Setup the specifications of the launch. This includes the instance type (e.g. t1.micro)
        // and the latest Amazon Linux AMI id available. Note, you should always use the latest
        // Amazon Linux AMI id or another of your choosing.
        LaunchSpecification launchSpecification = new LaunchSpecification();

        // image id for the FTA box is ami-08bf41e441c3eca20
        launchSpecification.setImageId("ami-08bf41e441c3eca20");
        launchSpecification.setInstanceType("t2.micro");

        // Add the security group to the request.
        ArrayList<String> securityGroups = new ArrayList<String>();
        securityGroups.add("all_traffic_open_insecure");
        launchSpecification.setSecurityGroups(securityGroups);

        // Add the launch specifications to the request.
        requestRequest.setLaunchSpecification(launchSpecification);

        //============================================================================================//
        //=========================== Getting the Request ID from the Request ========================//
        //============================================================================================//

        // Call the RequestSpotInstance API.
        RequestSpotInstancesResult requestResult = ec2.requestSpotInstances(requestRequest);
        List<SpotInstanceRequest> requestResponses = requestResult.getSpotInstanceRequests();

        // Setup an arraylist to collect all of the request ids
        // we want to watch hit the running state.
        ArrayList<String> spotInstanceRequestIds = new ArrayList<String>();

        // Add all of the request ids to the hashset, so we can determine when they hit the
        // active state.
        for (SpotInstanceRequest requestResponse : requestResponses) {
            System.out.println("Created Spot Request: "+requestResponse.getSpotInstanceRequestId());
            spotInstanceRequestIds.add(requestResponse.getSpotInstanceRequestId());
        }

        //============================================================================================//
        //=========================== Determining the State of the Spot Request ======================//
        //============================================================================================//

        // Create a variable that will track whether there are any requests still in the open state.
        boolean anyOpen;

        // Initialize variables.
        ArrayList<String> instanceIds = new ArrayList<String>();

        do {
            // Create the describeRequest with tall of the request id to monitor (e.g. that we started).
            DescribeSpotInstanceRequestsRequest describeRequest = new DescribeSpotInstanceRequestsRequest();
            describeRequest.setSpotInstanceRequestIds(spotInstanceRequestIds);

            // Initialize the anyOpen variable to false, which assumes there are no requests open unless
            // we find one that is still open.
            anyOpen=false;

            try {
                // Retrieve all of the requests we want to monitor.
                DescribeSpotInstanceRequestsResult describeResult = ec2.describeSpotInstanceRequests(describeRequest);
                List<SpotInstanceRequest> describeResponses = describeResult.getSpotInstanceRequests();

                // Look through each request and determine if they are all in the active state.
                for (SpotInstanceRequest describeResponse : describeResponses) {
                    // If the state is open, it hasn't changed since we attempted to request it.
                    // There is the potential for it to transition almost immediately to closed or
                    // cancelled so we compare against open instead of active.
                    if (describeResponse.getState().equals("open")) {
                        anyOpen = true;
                        break;
                    }

                    // Add the instance id to the list we will eventually terminate.
                    instanceIds.add(describeResponse.getInstanceId());
                }
            } catch (AmazonServiceException e) {
                // If we have an exception, ensure we don't break out of the loop.
                // This prevents the scenario where there was blip on the wire.
                anyOpen = true;
            }

            try {
                // Sleep for 60 seconds.
                Thread.sleep(60*1000);
            } catch (Exception e) {
                // Do nothing because it woke up early.
            }
        } while (anyOpen);

        //============================================================================================//
        //====================================== Canceling the Request ==============================//
        //============================================================================================//

        try {
            // Cancel requests.
            CancelSpotInstanceRequestsRequest cancelRequest = new CancelSpotInstanceRequestsRequest(spotInstanceRequestIds);
            ec2.cancelSpotInstanceRequests(cancelRequest);
        } catch (AmazonServiceException e) {
            // Write out any exceptions that may have occurred.
            System.out.println("Error cancelling instances");
            System.out.println("Caught Exception: " + e.getMessage());
            System.out.println("Reponse Status Code: " + e.getStatusCode());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Request ID: " + e.getRequestId());
        }

        //============================================================================================//
        //=================================== Terminating any Instances ==============================//
        //============================================================================================//
        try {
            // Terminate instances.
            TerminateInstancesRequest terminateRequest = new TerminateInstancesRequest(instanceIds);
            ec2.terminateInstances(terminateRequest);
        } catch (AmazonServiceException e) {
            // Write out any exceptions that may have occurred.
            System.out.println("Error terminating instances");
            System.out.println("Caught Exception: " + e.getMessage());
            System.out.println("Reponse Status Code: " + e.getStatusCode());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Request ID: " + e.getRequestId());
        }

    }

}