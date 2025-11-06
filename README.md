###  Web Automation Testing Project Description

				Project ini menyediakan framework lengkap untuk Web Automation Testing menggunakan Java + Selenium + TestNG, dengan mekanisme reporting, test data management, error handling, serta implementasi Page Object Model design pattern. Project ini berfokus pada pengujian User Registration flow, Login flow, dan Add to Cart flow. Selain itu project ini juga menambahkan automation API Testing menggunakan RestAssured yang mana reports dan test suite nya terpisah dengan web tapi bisa juga di gabungkan jika ingin menjalankan test secara pararel yang akan membuat report nya menjai 1.
				
---

###  Requirements & Tech Stack

				**Java 17**
				**Appium 2.11.2+**
				**Node.js v24.4.1+**
				**Xcode** (for iOS testing)
				**Eclipse IDE** / **IntelliJ IDEA**
       		    **Browser** (Sesuaikan dengan preferensi)

 ---

###	Git Workflow

       			**Jalankan perintah berikut untuk meng-clone repository ke local machine:**
        
				git clone <(https://github.com/kennyRamadhan/automation_web_selenium_doitpay)>
				cd <NAMA_FOLDER_PROJECT>
	
				
		        **Selalu Mulai dari Branch main Terbaru**
		        Sebelum memulai task baru, update branch main terlebih dahulu:
				
				git checkout main
				git pull origin main
	
				**Buat Branch Baru untuk Setiap Task**
		        Gunakan branch baru untuk setiap script atau task baru.
		        Format:
		        <tipe>/<nama_fitur_atau_task>
						
						Prefix branch yang disarankan:
		        feature/ → untuk penambahan test case baru
		        bugfix/ → untuk perbaikan script
		        hotfix/ → untuk perbaikan darurat
		        refactor/ → untuk perapihan kode
						
				**Commit & Push**
       			Setelah menambahkan atau memodifikasi script, lakukan commit:
				
				git add .
				git commit -m "Add: test case login with valid credential"
				Prefix commit yang disarankan:
        
		        Add: – menambahkan test case atau page baru
		        Update: – melakukan perubahan minor
		        Fix: – memperbaiki bug
		        Refactor: – membersihkan atau merapikan kode
		
		 			  Push branch ke remote repository:
						git push origin feature/login-test
			
		  			**Pull Request (PR)**
		        Buka repository di GitHub/GitLab.
		  
		        Masuk ke tab Pull Requests.
		        Klik New Pull Request.
		        Pilih:
		        Base branch: main
		        Compare branch: feature/<your-branch>
		        Tambahkan judul dan deskripsi yang jelas, lalu klik Create Pull Request.
		        Pastikan anda telah mereview script sendiri sebelum membuat PR.
					
						**Review & Merge**
		
		        Tunggu review/approval dari QA Lead atau maintainer repository.
		        Setelah disetujui dan digabung ke main, lakukan cleanup branch:
		        
		        git branch -d feature/<short-task-name>
		        git push origin --delete feature/<short-task-name>
		        git checkout main
		        git pull origin main
		        
 				**Delete the Branch (Cleanup)**
	 			git branch -d feature/<short-task-name>          # delete locally
				git push origin --delete feature/<short-task-name>  # delete on remote
				Then pull the latest main to prepare for your next task:
				git checkout main
				git pull origin main


				**Workflow Git**
				git clone <URL_REPO>
				cd <PROJECT_FOLDER>
				git checkout -b feature/search-client
				git add .
				git commit -m "Add: automation script for search client"
				git push origin feature/search-client
				Pull Request 
				Wait For Approval Merge
				Merge Approved
				git branch -d feature/<short-task-name>          # delete locally
				git push origin --delete feature/<short-task-name>  # delete on remote
				git checkout main
				git pull origin main

### Automation Tools

		        Selenium WebDriver – untuk menjalankan automation di browser
		        
		        TestNG – untuk test management dan parallel execution
		        
		        ExtentReports – untuk reporting HTML yang interaktif
---
        	
### Test Framework

		        Framework berbasis TestNG dengan struktur modular dan maintainable
		        Menggunakan Page Object Model (POM) untuk pemisahan antara test logic dan UI elements
		        Dilengkapi dengan:
		    
		        Error handling
		        Logging
		        Test data management
		        Reporting mechanism
		        JavaDoc comments untuk dokumentasi
---

### Menggunakan ExtentReports untuk menghasilkan laporan HTML interaktif berisi:

		        Reports hanya akan ter generate jika menjalankan test suite
		        Ringkasan hasil eksekusi
		        Log step-by-step
		        Screenshots otomatis (untuk test step,test success dan test gagal)
		        Informasi sistem & waktu eksekusi
		        Contoh Report yang sudah ter generate ada di folder reports/summary-report
		        
		    

---

###  Environment Setup

		        Instalasi Tools yang Dibutuhkan
		        Java JDK 17 – Download di sini
		        Maven – Download di sini
		        Browser WebDriver (ChromeDriver, GeckoDriver, dll)
		        IDE: Eclipse atau IntelliJ IDEA
---


###  Menjalankan Test
		        Melalui Terminal
		        Command	Deskripsi :
		        
		        mvn clean test	Menjalankan semua test berdasarkan testng.xml
		        mvn clean test -Dsurefire.suiteXmlFiles=testng.xml	Menjalankan suite tertentu
		        mvn clean test -Dtest=ClassNameTest	Menjalankan test berdasarkan nama class
		        mvn clean test -Dtest=ClassNameTest#methodName	Menjalankan test berdasarkan method
		        mvn clean install -DskipTests	Compile project tanpa menjalankan test
		        mvn clean test -X	Menjalankan test dengan debug output
		
		        Melalui Eclipse/IntelliJ :
		
		        Run All Tests: Klik kanan pada testng.xml → Run As → TestNG Suite
		        Run Single Class: Klik kanan pada test class → Run As → TestNG Test
---
### Project Structure

				
						src/
		         ├── main/java
		         │    ├── com/kenny/doitpay/automation/Config/       # Konfigurasi environment & driver
		         │    ├── com/kenny/doitpay/automation/Helper        # Utility/helper functions 
		         │    ├── com/kenny/doitpay/automation/Listeners     # Listener untuk logging, reporting, screenshot
		         │    ├── com/kenny/doitpay/automation/Page          # Page Object Models (POM) 
		         |    ├── com/kenny/doitpay/automation/Resources     # Data Driven Testing
		         │
		         └── test/java
		              └──  com/kenny/doitpay/automation/Web           # Test cases (Registration, Login, Add to Cart)
		              ├──  com/kenny/doitpay/automation/API           # Test API
		        reports/
		         ├── summary-reports/                                 # Laporan hasil eksekusi test dan Screenshot untuk test yang berhasil maupun gagal
		               
				  
						pom.xml → Maven dependencies & build configuration
						WebSuite.xml = TestNG suite & listener configuration untuk Web
		                APISuite.xml = TestNG suite & listener configuration untuk API
---

### Test Execution Flow

		        Jalankan suite menggunakan testng.xml
		        Listener otomatis aktif (logging, screenshot, reporting)
		        BaseTest melakukan inisialisasi driver dan environment
		        Test case dijalankan berdasarkan Page Object Model
		        Hasil eksekusi tersimpan dalam folder reports/summary-reports

### Notes

				Jalankan git pull sebelum memulai task baru
		        Gunakan branch terpisah untuk setiap fitur
		        Gunakan naming convention yang konsisten
		        Jalankan test secara lokal sebelum push ke remote
		        Tambahkan komentar dan JavaDoc agar kode mudah dipelihara
---



### Reports 

				Report menggunakan 2 framework yaitu Extent Report & Allure Report
				untuk Extent Report ada di folder reports/summary-report
				untuk Allure Report ada di folder allure-result
				untuk membuka report html Allure Report buka terminal dan masukan :
				allure serve allure-results

### License

       			 Project ini dibuat untuk keperluan Portofolio Sebagai QA Engineer
---
				
				
				


