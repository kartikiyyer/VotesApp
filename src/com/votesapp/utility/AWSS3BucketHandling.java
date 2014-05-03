package com.votesapp.utility;

import java.io.File;
import java.util.Calendar;

import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONObject;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.sun.jersey.core.header.FormDataContentDisposition;

public class AWSS3BucketHandling {
	private String ipaddress="ds053838.mongolab.com";
	public String addS3BucketObjects( File fileobject,FormDataContentDisposition contentDispositionHeader,
			String pollid) throws Exception{
		String response="fail";

		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		s3.setRegion(usWest2);
		String bucketName="votesappbucket";
		
		Calendar time = Calendar.getInstance();
		String fileName=Integer.toString(time.get(Calendar.HOUR))+Integer.toString(time.get(Calendar.MINUTE))+Integer.toString(time.get(Calendar.MILLISECOND))+contentDispositionHeader.getFileName();
		System.out.println("fileName: "+fileName);
		
		
		try {
			System.out.println("\nUploading a new object to S3...");
			System.out.println("upload file name:: "+fileName);

			PutObjectRequest putObj=new PutObjectRequest(bucketName, fileName, fileobject);

			//making the object Public
			putObj.setCannedAcl(CannedAccessControlList.PublicRead);
			s3.putObject(putObj);

			AmazonS3Client awss3client=new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
			awss3client.getResourceUrl(bucketName, fileName);

			System.out.println("File uploaded on S3 - location: "+bucketName+" -> "+fileName);
			JSONObject obj1 = new JSONObject();
			obj1.put("pollid", pollid);
			obj1.put("downloadlink", awss3client.getResourceUrl(bucketName, fileName));

			System.out.println("result str:  "+obj1);
			String res=addMetaDataToDB(obj1);
			response=res;

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		return response;
	}
	
	
	public String addMetaDataToDB(JSONObject metadata){
		String result="fail";
		try{
			MongoClient mongo = new MongoClient( ipaddress , 53838 );
			DB db = mongo.getDB("votesapp");
			boolean auth = db.authenticate("admin", "password@123".toCharArray());
			if(auth){
				DBCollection table = db.getCollection("polls");
				BasicDBObject newDocument  = new BasicDBObject();
				BasicDBObject whereQuery  = new BasicDBObject();
				newDocument .append("$set", new BasicDBObject().append("poll_media_link", metadata.get("downloadlink")));
				whereQuery.append("_id", new ObjectId(metadata.getString("pollid")));
				table.update(whereQuery, newDocument);
				result="success";
			}else{
				System.out.println("can not connect");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return (result);
	}
	
}
