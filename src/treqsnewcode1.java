/* 
Anyone can feel free to clone the repository and contribute to improve the project 
overall. Right now, it works for a very standard way of generating dockerfiles of
minimal size for Python and Go projects. However, the code is still subject to test and
further development into a usable JAR file. Any suggestions for generalizing it 
further are more than welcome.
JUST DON'T WRECK ME FOR NOT WRITING BEAUTIFUL CODE :-P
IT MAY GET A LITTLE BIT SLOPPY !!!!  CAUTION !!!!
*/

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class treqsnewcode1
{
	public static void main(String[] args) throws Exception
	{
		Scanner scan = new Scanner(System.in);

		System.out.println();

		treqs_banner();

		System.out.println();

		// Now, we will work with multiple source files

		int no_of_source_files = 0;

		System.out.println("Enter the no of source code files:");

		no_of_source_files = scan.nextInt();

		System.out.println();

		String[] file_name = new String[no_of_source_files];

		System.out.println("Enter the names(paths) of the file:");

		for(int i = 0; i < no_of_source_files; i++)
		{
			file_name[i] = scan.next();
		}

		System.out.println();

		//	String file_name = scan.nextLine();

    	//  Now, instead of a single file name string, it uses an array 

    	//  Now, we need to access the extensions from the string names of the file individually


		System.out.println("Enter a name for the new project directory :" );

		String master_folder_name = new String();

		master_folder_name = scan.next();

		System.out.println();

		String maintain_final_key = new String();

		System.out.println("Enter the contributor information :" );

		maintain_final_key = scan.next();

		System.out.println();
		
		String user_email_id = new String();

		System.out.println("Enter the your email-id for reference :");

		user_email_id = scan.next();

		System.out.println();

		// Need to ask for this once, so, better ask the name here.

		File master_app_dir = new File(master_folder_name);

				if(master_app_dir.exists())
				{
						master_app_dir.delete();
				}

				try
				{
						master_app_dir.mkdir();

						System.out.println("Master app directory has been created !!!");

						System.out.println();
				}
				catch(Exception e)
				{
						e.printStackTrace();
				}

	
		String[] the_file_extension = new String[no_of_source_files];

		String temp_file_name = new String();

		String temp_file_extension = new String();

		for(int i = 0; i < no_of_source_files; i++)
		{
			temp_file_name = file_name[i];

			the_file_extension[i] = get_extension(temp_file_name);  // The type of argument in the function has changed
		// Retrieved the extension
		}

		
		// Change the type of argument fed into the function

		// CHANGED

		String from_key = "FROM";
		String run_key = "RUN";
		String add_key = "ADD";
		String workdir_key = "WORKDIR";
		String expose_key = "EXPOSE";
		String cmd_key = "CMD";
		String copy_key = "COPY";
		String maintain_key = "MAINTAINER";
		String python_key = "python";
		String server_py = "server.py";
		String entry_point = "ENTRYPOINT";
		String golang_key ="golang";
		String server_go =" server.go";
		String ruby_key = "ruby";


		String test_dependencies = new String();

		int counter = 1;

		//    The conversion of file to string begins //

		for(int i = 0; i < no_of_source_files; i++)
		{
				FileReader f_reader = new FileReader(file_name[i]);   // Using the files one-by-one

				BufferedReader b_reader = new BufferedReader(f_reader);

				String converted_string = "";

				String line = b_reader.readLine();

				while(line != null)
				{
						converted_string+= line;

						converted_string = converted_string +"\n";  // This line was added to resolve a bug

						//	converted_string+=System.getProperty("line.terminator");

						line = b_reader.readLine();
				}

				b_reader.close();

				// The filer-reading part closes
		

				String[] package_installer_info = package_recognizer(the_file_extension[i], file_name[i]);
				// The arguments need not be of String array type as we are actually passing on a value basis


				String preprocess_keyword = new String();

				preprocess_keyword = getting_preprocess(the_file_extension[i]);

		

				//String delimiter_value ="\\r?\\n";

				String[] broken_string = converted_string.trim().split("\\r?\\n|\\s+|\\t", -1);

		

				String import_string_val = preprocess_keyword;   // We can use it from here.


       

				String[] dependencies_list_final = dependencies_acquire(broken_string, the_file_extension[i]);

				int no_of_dependencies = dependencies_list_final.length;
	

		
				System.out.println("Enter a default name for the Docker image for code file number " + counter + " :");

				counter++;

				String docker_image_name = scan.next();


		

			//	System.out.println("Enter a name for the code folder :");

			// This time I'm keeping the name of the source code and the app folder

				String code_folder_name = new String();

				code_folder_name = file_name[i].split("\\.")[0];

				File app_dir = new File(master_folder_name + "/" + code_folder_name );

				if(app_dir.exists())
				{
						app_dir.delete();
				}

				try
				{
						app_dir.mkdir();
				}
				catch(Exception e)
				{
						e.printStackTrace();
				}

		

				File code_file = new File(file_name[i]);

				String src_folder_name = "src";

				File src_file_dir = new File(master_folder_name + "/" + code_folder_name + "/" + src_folder_name);

				if(src_file_dir.exists())
				{
					src_file_dir.delete();
				}

				try
				{
					src_file_dir.mkdir();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

				File destination_code_file = new File(master_folder_name + "/" + code_folder_name + "/" + src_folder_name + "/" + file_name[i]);

				try
				{	
						if(!destination_code_file.exists())
						{
								destination_code_file.createNewFile();
						}
				}
				catch(Exception e)
				{
						e.printStackTrace();
				}

				try
				{
						Files.copy(code_file.toPath(), destination_code_file.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
				catch(Exception e)
				{
						e.printStackTrace();
				}

	   			// We have to decide the nature of the requirements file in case of the different languages

				// For Golang it'll be a Gopkg.toml and Gopkg.lock files

				if(the_file_extension[i].equals("py"))
				{
						File new_req_file = new File(master_folder_name + "/" + code_folder_name + "/requirements.txt");

						if(new_req_file.exists())
						{
								new_req_file.delete();
						}
			
						try
						{
								new_req_file.createNewFile();
						}
						catch(Exception e)
						{
								e.printStackTrace();
						}

						try
						{	
								FileWriter write_file = new FileWriter(new_req_file);

								BufferedWriter write_buff = new BufferedWriter(write_file);

								for(String str_element : dependencies_list_final)
								{
										write_buff.write(str_element + System.lineSeparator());
								}

								write_buff.close();
						}
						catch(Exception e)
						{
								e.printStackTrace();	
						}
				}
				else if(the_file_extension[i].equals("go"))
				{

						File new_req_file = new File(master_folder_name + "/" + code_folder_name + "/requirements.txt");

					

						// Try creating a vendor folder
						String vendor_folder_name = "vendor";

						File vendor_dir = new File(master_folder_name + "/" + code_folder_name +"/" + vendor_folder_name);

						if(vendor_dir.exists())
						{
							vendor_dir.delete();
						}

						try
						{
							vendor_dir.mkdir();
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}

						// vendor folder created

						try
						{
								FileWriter reqfilewrite = new FileWriter(new_req_file);

								BufferedWriter reqwriterbuffr = new BufferedWriter(reqfilewrite);

								for(String str_element : dependencies_list_final)
								{
									if(!str_element.equals("\\s+") || !str_element.equals("\\r?\\n") || !str_element.equals("\\t") || !str_element.equals(" "))
									{
											reqwriterbuffr.write(str_element);
									}

									reqwriterbuffr.write(System.lineSeparator());
									
								}

								reqwriterbuffr.close();

						}
						catch(Exception e)
						{
								e.printStackTrace();

								System.out.println("Nothing could be written into the Go file !!!!");
						}
				}

				else if(the_file_extension[i].equals("rb"))
				{
					/* DO SOMETHING
					   PLOXX */
				}
		
				Path currentRelativePath = Paths.get("");

				String the_current_address = currentRelativePath.toAbsolutePath().toString();

				String workdir_final_key = new String();

				workdir_final_key = "./" + workdir_key;

				File new_dockerfile = new File(master_folder_name + "/" + code_folder_name + "/Dockerfile");

				if(new_dockerfile.exists())
				{
						new_dockerfile.delete();
				}

				try
				{
						new_dockerfile.createNewFile();
				}
				catch(Exception e)
				{
						e.printStackTrace();
				}

				try
				{
						FileWriter docker_file_writer = new FileWriter(new_dockerfile);

						BufferedWriter docker_writer = new BufferedWriter(docker_file_writer);

						docker_writer.write(from_key + " " + package_installer_info[0] + System.lineSeparator()); // Starting with a very small base image

						docker_writer.write(System.lineSeparator() + maintain_key + " " + maintain_final_key + " <" + user_email_id + ">" + System.lineSeparator());

						docker_writer.write(System.lineSeparator() + add_key + " " + "src/" + file_name[i] + " /");

						// WE DON'T NEED WORK DIRECTORY BECAUSE THAT'S ALREADY TAKEN CARE OF //

						if(the_file_extension[i].equals("py"))
						{
								docker_writer.write(System.lineSeparator() + run_key + " " + package_installer_info[1] + "/requirements.txt" + System.lineSeparator());
						}
						else if(the_file_extension[i].equals("go"))
						{
								docker_writer.write(System.lineSeparator() + run_key + " " + package_installer_info[1] + " " + "github.com/golang/dep/cmd/dep" + System.lineSeparator());

								docker_writer.write(System.lineSeparator() + run_key + " dep init" + System.lineSeparator());

								docker_writer.write(System.lineSeparator() + add_key + " " + "Gopkg.toml Gopkg.toml");

								docker_writer.write(System.lineSeparator() + add_key + " " + "Gopkg.lock Gopkg.lock");

								docker_writer.write(System.lineSeparator());

								docker_writer.write(System.lineSeparator() + run_key + " dep ensure -vendor-only");
						}
						else if(the_file_extension[i].equals("rb"))
						{
								System.out.println("Nothing decided yet !!!");
						}

						docker_writer.write(System.lineSeparator() + workdir_key + " /");    // The default working directory has been set to ROOT

						docker_writer.write(System.lineSeparator() + expose_key + " 8080" + System.lineSeparator()); // By default, it's taken as the port 8080, it cant be changed manually later.

						// Need to mention the file's executables properly

						docker_writer.write(System.lineSeparator() + entry_point + "[\"" + file_name[i].split("\\.")[0] + "\"]" );

						if(the_file_extension[i].equals("py"))
						{
								docker_writer.write(System.lineSeparator() + cmd_key + " [\"" + "src/" + file_name[i] + "\"]");
						}
						else if(the_file_extension[i].equals("go"))
						{
								docker_writer.write(System.lineSeparator() + cmd_key + " [\"" + "src/" + file_name[i] + "\"]");
						}
						else if(the_file_extension[i].equals("rb"))
						{
								docker_writer.write(System.lineSeparator() + cmd_key + " [\"" + "src/" + file_name[i] + "\"]");
						}

						docker_writer.close();
				}
				catch(Exception e)
				{
						e.printStackTrace();
				}

				File docker_compose_file = new File(master_folder_name + "/" + code_folder_name + "/docker-compose.yml");

 				if(docker_compose_file.exists())
 				{
 						docker_compose_file.delete();
 				}

 				try
 				{
 						docker_compose_file.createNewFile();
 				}
 				catch(Exception e)
 				{
 						e.printStackTrace();
 				}

 				try
 				{
 						FileWriter docker_compose_fw = new FileWriter(docker_compose_file);

 						BufferedWriter docker_compose_buffw = new BufferedWriter(docker_compose_fw);

 						docker_compose_buffw.write("version: '3'" + System.lineSeparator());

 						docker_compose_buffw.write("services:" + System.lineSeparator());

 						docker_compose_buffw.write(" " + code_folder_name + ":" + System.lineSeparator());

 						docker_compose_buffw.write("  " + "images: " + docker_image_name + System.lineSeparator());

 						docker_compose_buffw.write("  " + "build: " + "." + System.lineSeparator());

 						docker_compose_buffw.write("   " + "context: " + "." + System.lineSeparator());

 						docker_compose_buffw.write("  " + "ports: " + System.lineSeparator());

 						docker_compose_buffw.write("   " + "8080:8080" + System.lineSeparator());

 						docker_compose_buffw.write("  " + "environment: " + System.lineSeparator());

 						docker_compose_buffw.write("   " + "-VIRTUAL_HOST=" + System.lineSeparator());

 						docker_compose_buffw.write("   " + "-VIRTUAL_PORT=8080" + System.lineSeparator());

 						docker_compose_buffw.write("  " + "volumes: " + System.lineSeparator());

 						docker_compose_buffw.close();

 				}
 				catch(Exception e)
 				{
 						e.printStackTrace();
 				}

 		
		}

		scan.close();
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	///////////          Getting the list of required dependencies          //////////////////
	/////////////////////////////////////////////////////////////////////////////////////////

	public static String[] dependencies_acquire(String[] broken_string/* int no_of_dependencies */, String the_file_extension)
	{
		List<String> dependencies_list = new ArrayList<String>();

		String import_ref = new String();

		String temp_string = new String();

		String from_keyword = new String();

		if(the_file_extension.equals("py"))
		{
			import_ref = "import";

			from_keyword = "from";

			for(int i = 0; i < broken_string.length - 1; i++)
			{
				if(import_ref.equals(broken_string[i]) || from_keyword.equals(broken_string[i]))
				{
					temp_string = broken_string[i + 1];

					dependencies_list.add(temp_string);

					i++;  // No need to read the same keyword again next time
				}
			}
		}

		// Code for python works just fine !!! Check for the GOLANG version.

		// The no of dependencies will be different for GOLANG

		// Need to work on this selection scheme.
		else if(the_file_extension.equals("go"))
		{
			import_ref = "import";

			int k;

			for(int i = 0; i < broken_string.length; i++)
			{
				k = i;

				int post_import_index; 

				if(import_ref.equals(broken_string[k]))
				{
					post_import_index = k + 1;

					while(!broken_string[post_import_index].equals(")"))
					{
						if(broken_string[post_import_index].equals("(") || broken_string[post_import_index].equals("\\s+") || broken_string[post_import_index].equals("\\r?\\n") || broken_string[post_import_index].equals("\\t") || broken_string[post_import_index].equals(" "))
						{
							post_import_index++;
						}   
						else
						{
							temp_string = broken_string[post_import_index];

							dependencies_list.add(temp_string);

							post_import_index++;
						}

					}
				}
			}
		}
		else if(the_file_extension.equals("rb"))
		{
			import_ref = "require";

			for(int i = 0; i < broken_string.length; i++)
			{
				if(import_ref.equals(broken_string[i]))
				{ 

					String new_temp_string = broken_string[i + 1];

					temp_string = new_temp_string.replaceAll("\'","");

					dependencies_list.add(temp_string);

					i++;  // Skipping an extra step, no need for it
				}
			}
		}

		String[] dependencies_list_final = (String[]) dependencies_list.toArray(new String[dependencies_list.size()]);

		return dependencies_list_final;

		// By using the length method  of the array, we get the no of dependencies
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	//////////////      Getting the extension from the source file      ////////////////////
	///////////////////////////////////////////////////////////////////////////////////////

	public static String get_extension(String file_name)
	{
		String file_extension = new String();

		file_extension = file_name.split("\\.")[1];

		return file_extension;
	}

	// It returns a full array of the extensions all the source code files entered

	////////////////////////////////////////////////////////////////////////////////////////
	/////////////         Setting it for different langauges          /////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	// This function doesn't need to be modified

	public static String[] package_recognizer(String file_extension, String file_name)
	{
		String[] package_info_array = new String[4];

		// 1. Stores the information about base image
		// 2. Stores the information about the package install
		// Both in string expressions.

		if(file_extension.equals("py"))
		{
			package_info_array[0] = "python:3.6-alpine";

			package_info_array[1] = "pip install -r";

			package_info_array[2] = "python";

			package_info_array[3] = file_name;
		}
		else if(file_extension.equals("go"))
		{
			package_info_array[0] = "golang:1.11.0-alpine3.8";

			package_info_array[1] = "go get -u";

			package_info_array[2] = "";

			package_info_array[3] = "";
		}
		else if(file_extension.equals("rb"))
		{
			package_info_array[0] = "ruby:2.5-onbuild";

			package_info_array[1] = "bundle install";

			package_info_array[2] = "";

			package_info_array[3] = file_name;
		}
		else
		{
			System.out.println("NOT A SUPPORTED FILE TYPE !!!");

			return null;
		}
		
		return package_info_array;

	}

	///////////////////////////////////////////////////////////////////////////////////
	//////////////    Getting the pre-processing directive    ////////////////////////
	/////////////////////////////////////////////////////////////////////////////////

	public static String getting_preprocess(String file_extension)
	{
		String the_preprocess = new String();

		if(file_extension.equals("py"))
		{
			the_preprocess = "import";
		}
		else if(file_extension.equals("go"))
		{
			the_preprocess = "import";
		}
		else if(file_extension.equals("rb"))
		{
			the_preprocess = "require";
		}

		return the_preprocess;

	}

	/////////////////////////////////////////////////////////////////////////////////
	////////////      Terminal banner code, just for fun XD     ////////////////////
	///////////////////////////////////////////////////////////////////////////////

	public static void treqs_banner()
	{
		System.out.println();
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
		System.out.println();
		System.out.println(" ,-------------------,         ,----------,  ,==========,     ,===========,         ____________      ");
		System.out.println(" '------- , ,--------'         |----------|  |=========='    / /---------|'|      //____________||    ");
		System.out.println("          | |                  |  |     | |  | |            | |          | |      | |                 ");
		System.out.println("          | |                  |  |     | |  | |            | |          | |      | |                 ");
		System.out.println("          | |          ,=====, |  |-----' |  |----------,   | |          | |      | |===========..    ");
		System.out.println("          | |          '=====' |  |-------'  |=========='   | |     ,,   | |      |____________, |    ");
		System.out.println("          | |                  |  | | |      | |            | |     | |  | |                   | |    ");        
		System.out.println("          | |                  |  |  | |     | |            | |      | | | |                   | |    ");
		System.out.println("          | |                  |  |   | |    |==========,    | | -------/ /,,,,   ,============, |    ");
		System.out.println("          '='                  '=='    '='   '=========='     '=========----====   |______________/'  ");
		System.out.println();
		System.out.println("-------------------------       !!!  Cuz  my  pun  game  is  strong  AF  !!!        ----------------------------");           
		System.out.println();
	}	

		
}