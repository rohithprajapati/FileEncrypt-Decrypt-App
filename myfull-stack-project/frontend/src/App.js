import { DeleteOutlined, DownloadOutlined, LockOutlined, UploadOutlined } from '@ant-design/icons';
import { Button, ConfigProvider, Form, Input, Layout, Menu, Modal, Select, Space, Switch, Typography, Upload, message } from 'antd';
import axios from 'axios';
import React, { useState } from 'react';
import './App.css';

const { Header, Content, Footer } = Layout;
const { Option } = Select;
const { Title, Paragraph } = Typography;

const App = () => {
  const [mode, setMode] = useState('encrypt');
  const [fileList, setFileList] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [downloadLink, setDownloadLink] = useState(null);
  const [darkMode, setDarkMode] = useState(false); // Dark mode state

  const handleUpload = ({ file }) => {
    if (file) {
      setFileList([file]);
      message.success(`${file.name} uploaded successfully.`);
    }
  };

  const handleModeChange = (value) => {
    setMode(value);
  };

  const handleSubmit = async (values) => {
    console.log('Submitted:', values);
    message.info(`Processing ${mode}ion for: ${fileList[0]?.name || 'text input'}`);

    if (fileList.length === 0) {
      message.error('Please upload a file before submitting.');
      return;
    }

    const formData = new FormData();
    formData.append('file', fileList[0]);
    formData.append('secretKey', values.key);

    try {
      const response = await axios.post(`http://localhost:8080/api/file/${mode}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
        responseType: 'arraybuffer' // Handle binary data
      });

      const blob = new Blob([response.data], { type: 'application/octet-stream' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = `${mode === 'encrypt' ? 'encrypted' : 'decrypted'}_${fileList[0].name}`;
      link.click();

      message.success(`${mode === 'encrypt' ? 'Encryption' : 'Decryption'} successful!`);
    } catch (error) {
      console.error('Error during file processing:', error);
      if (error.response) {
        message.error(`Error during ${mode}: ${error.response.data.message || error.message}`);
      } else {
        message.error(`Error during ${mode}: ${error.message}`);
      }
    }
  };

  const handleDelete = () => {
    setFileList([]);
    setDownloadLink(null);
    message.success('File removed successfully.');
  };

  const showInfoModal = () => {
    setIsModalVisible(true);
  };

  const handleModalOk = () => {
    setIsModalVisible(false);
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
  };

  const toggleDarkMode = (checked) => {
    setDarkMode(checked);
  };

  return (
    <ConfigProvider
      theme={{
        token: {
          colorPrimary: '#1890ff', // Change primary color
          // Add more customization here (e.g., fonts, other colors)
        },
        mode: darkMode ? 'dark' : 'light', // Toggle mode based on darkMode state
      }}
    >
      <Layout className="layout" style={{ minHeight: '100vh' }}>
        <Header>
          <div className="logo">File Encryptor</div>
          <Menu theme={darkMode ? 'dark' : 'light'} mode="horizontal" defaultSelectedKeys={['1']}>
            <Menu.Item key="1">Home</Menu.Item>
            <Menu.Item key="2">About</Menu.Item>
            <Menu.Item key="3" onClick={showInfoModal}>How It Works</Menu.Item>
          </Menu>

          {/* Dark Mode / Light Mode Switch */}
          <div style={{ float: 'right', marginTop: '10px' }}>
            <span style={{ marginRight: '8px' }}>Light</span>
            <Switch checked={darkMode} onChange={toggleDarkMode} />
            <span style={{ marginLeft: '8px' }}>Dark</span>
          </div>
        </Header>

        <Content style={{ padding: '50px', background: darkMode ? '#001529' : '#fff' }}>
          <div className="site-layout-content" style={{ minHeight: '100%' }}>
            <Title level={2} style={{ textAlign: 'center', color: darkMode ? '#fff' : '#000' }}>File Encryption & Decryption</Title>
            <Paragraph style={{ textAlign: 'center', color: darkMode ? '#fff' : '#000' }}>
              Secure your files with advanced AES encryption and user-friendly features.
            </Paragraph>

            <Form layout="vertical" onFinish={handleSubmit}>
              <Form.Item
                label="Mode"
                name="mode"
                rules={[{ required: true, message: 'Please select a mode!' }]}
              >
                <Select onChange={handleModeChange} defaultValue={mode}>
                  <Option value="encrypt">Encrypt</Option>
                  <Option value="decrypt">Decrypt</Option>
                </Select>
              </Form.Item>

              <Form.Item
                label="Key"
                name="key"
                rules={[{ required: true, message: 'Please enter a key!' }]}
              >
                <Input.Password placeholder="Enter secure key" prefix={<LockOutlined />} />
              </Form.Item>

              <Form.Item label="Upload File" valuePropName="fileList">
                <Upload
                  beforeUpload={() => false}
                  onChange={handleUpload}
                  fileList={fileList}
                >
                  <Button icon={<UploadOutlined />} style={{ backgroundColor: darkMode ? '001529#' : '#1890ff' }}>
                    Click to Upload
                  </Button>
                </Upload>
              </Form.Item>

              {fileList.length > 0 && (
                <Space>
                  <Button type="dashed" icon={<DeleteOutlined />} onClick={handleDelete}>
                    Remove File
                  </Button>
                </Space>
              )}

              <Form.Item>
                <Button type="primary" htmlType="submit" style={{ backgroundColor: darkMode ? '#001529' : '#1890ff' }}>
                  {mode === 'encrypt' ? 'Encrypt' : 'Decrypt'}
                </Button>
              </Form.Item>
            </Form>

            {downloadLink && (
              <div style={{ marginTop: '20px', textAlign: 'center' }}>
                <Button type="link" href={downloadLink} download icon={<DownloadOutlined />}>
                  Download Processed File
                </Button>
              </div>
            )}
          </div>
        </Content>

        <Footer style={{ textAlign: 'center', background: darkMode ? '#001529' : '#fff', color: darkMode ? '#fff' : '#000' }}>
          File Encryption App ©2024 Created by Rohith Prajapati
        </Footer>

        <Modal
          title="How It Works"
          visible={isModalVisible}
          onOk={handleModalOk}
          onCancel={handleModalCancel}
          footer={null}
        >
          <Typography>
            <Title level={4}>Encryption</Title>
            <Paragraph>
              Select the "Encrypt" mode, upload your file, and provide a secure key. The application will encrypt the file using the AES algorithm and return the encrypted version.
            </Paragraph>

            <Title level={4}>Decryption</Title>
            <Paragraph>
              Select the "Decrypt" mode, upload your encrypted file, and provide the same key used during encryption. The application will decrypt the file and return the original content.
            </Paragraph>

            <Title level={4}>Key Management</Title>
            <Paragraph>
              Ensure you save your secure key. Losing the key will make it impossible to decrypt your files.
            </Paragraph>
          </Typography>
        </Modal>
      </Layout>
    </ConfigProvider>
  );
};

export default App;
